package com.example.autodeal.user.service;

import com.example.autodeal.exception.UserAlreadyExistsException;
import com.example.autodeal.exception.UserNotFoundException;
import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.UserRole;
import com.example.autodeal.user.model.VerificationToken;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.UserRoleRepository;
import com.example.autodeal.user.repository.VerificationTokenRepository;
import com.example.autodeal.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UserService userService;

    private UserMapper userMapper;
    private UserModel userModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userMapper = new UserMapper();

        UserRole userRoleUser = new UserRole();
        userRoleUser.setName("ROLE_USER");
        when(userRoleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRoleUser));

        userModel = new UserModel();
        userModel.setEmail("jan.kot@gmail.com");
        userModel.setPassword("hashedPassword");
        userModel.setRoles(Collections.singleton(userRoleUser));

        when(userRepository.findByEmail("jan.kot@gmail.com")).thenReturn(Optional.of(userModel));
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VerificationToken mockToken = new VerificationToken();
        when(verificationTokenRepository.save(any(VerificationToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(verificationTokenRepository.findByToken("some-token")).thenReturn(Optional.of(mockToken));

        userService = new UserService(userRepository, userRoleRepository, passwordEncoder, notificationService, verificationTokenRepository, userMapper);
    }


    @Test
    void whenFindAllUsers_thenReturnsListOfUsers() {
        UserModel user1 = new UserModel();
        user1.setEmail("user1@example.com");
        UserModel user2 = new UserModel();
        user2.setEmail("user2@example.com");
        List<UserModel> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserModel> foundUsers = userService.findAllUsers();

        assertThat(foundUsers).hasSize(2);
        assertThat(foundUsers.get(0).getEmail()).isEqualTo("user1@example.com");
        assertThat(foundUsers.get(1).getEmail()).isEqualTo("user2@example.com");
    }

    @Test
    void whenValidId_thenUserShouldBeFound() {
        UserModel user = new UserModel();
        user.setId(1);
        user.setEmail("user@example.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserModel found = userService.findUserById(1);

        assertThat(found.getId()).isEqualTo(user.getId());
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void whenInvalidId_thenThrowException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findUserById(99));
    }

    @Test
    public void whenAddUser_thenUserIsSaved() {
        UserModel newUser = new UserModel();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");

        userService.addUser(newUser);

        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void whenEditUser_thenUserIsSaved() {
        UserModel user = new UserModel();
        user.setId(1);
        user.setEmail("user@example.com");
        user.setPassword("password");

        userService.saveEditUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void whenValidEmail_thenUserShouldBeFound() {
        String email = "jan.kot@gmail.com";
        UserModel found = userService.findUserByEmail(email);

        assertEquals(found.getEmail(), email);
    }

    @Test
    public void whenInvalidEmail_thenThrowUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.findUserByEmail("invalid@example.com");
        });
    }

    @Test
    public void whenRegisterNewUser_thenUserIsSavedAndCookieIsCreated() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("newuser@example.com");
        signUpDto.setPassword("password");

        MockHttpServletResponse response = new MockHttpServletResponse();

        UserModel registeredUser = userService.registerNewUser(signUpDto, response);

        assertNotNull(registeredUser);
        verify(userRepository, times(1)).save(any(UserModel.class));
        assertEquals("session-id", response.getCookies()[0].getName());
    }

    @Test
    public void whenRegisterExistingUser_thenThrowUserAlreadyExistsException() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("jan.kot@gmail.com");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerNewUser(signUpDto, response);
        });
    }

    @Test
    public void whenDeleteUser_thenUserShouldBeDeleted() {
        Integer userId = 1;
        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void loadUserByUsername_UserFound_ReturnsUserDetails() {
        UserDetails userDetails = userService.loadUserByUsername("jan.kot@gmail.com");

        assertEquals("jan.kot@gmail.com", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    public void loadUserByUsername_UserNotFound_ThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.loadUserByUsername("notfound@example.com");
        });
    }

    @Test
    void shouldReturnEmptyListIfNothingInDb() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRoleRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserModel> result = userService.findAllUsers();

        assertThat(result).isEmpty();
    }

    @Test
    void whenValidToken_thenUserIsEnabled() {
        UserModel user = new UserModel();
        user.setEnabled(false);

        VerificationToken token = new VerificationToken();
        token.setToken("valid-token");
        token.setUserModel(user);

        when(verificationTokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

        userService.confirmUserRegistration("valid-token");

        assertTrue(user.getEnabled());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void whenInvalidToken_thenThrowException() {
        when(verificationTokenRepository.findByToken("invalid-token")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.confirmUserRegistration("invalid-token"));
    }
}