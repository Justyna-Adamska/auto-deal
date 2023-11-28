package com.example.autodeal.user.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.UserRole;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserService userService;

    private UserModel userModel;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        UserRole userRoleUser = new UserRole();
        userRoleUser.setName("ROLE_USER");
        when(userRoleRepository.findByName("ROLE_USER")).thenReturn(userRoleUser);

        userModel = new UserModel();
        userModel.setEmail("jan.kot@gmail.com");
        userModel.setPassword("hashedPassword");
        userModel.setRoles(Collections.singleton(userRoleUser));

        when(userRepository.findByEmail("jan.kot@gmail.com")).thenReturn(Optional.of(userModel));
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
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
    public void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("notfound@example.com");
        });
    }


}
