package com.example.autodeal;

import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.UserRole;
import com.example.autodeal.user.model.VerificationToken;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.UserRoleRepository;
import com.example.autodeal.user.repository.VerificationTokenRepository;
import com.example.autodeal.user.service.NotificationService;
import com.example.autodeal.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = AutoDealApplication.class)
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {"/sql/delete-test-data.sql"})
public class UserRegistrationAndLoginIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private HttpServletResponse response;

    @BeforeEach
    public void setup() {

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        UserRole userRole = userRoleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    UserRole newUserRole = new UserRole();
                    newUserRole.setName("ROLE_USER");
                    return userRoleRepository.save(newUserRole);
                });
    }

    @AfterEach
    public void cleanup() {
        verificationTokenRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void testUserRegistrationAndLoginAndDeleteAccount() {

        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setEmail("test@example.com");
        signUpDto.setPassword("Test123!");
        signUpDto.setFirstName("Test");
        signUpDto.setLastName("User");
        signUpDto.setPhone("123-456-7890");

        UserModel savedUser = userService.registerNewUser(signUpDto, response);
        assertNotNull(savedUser);
        assertFalse(savedUser.getEnabled());

        VerificationToken verificationToken = verificationTokenRepository.findByUserModel(savedUser)
                .orElseThrow(AssertionError::new);
        userService.confirmUserRegistration(verificationToken.getToken());

        UserModel confirmedUser = userRepository.findById(savedUser.getId()).orElseThrow(AssertionError::new);
        assertTrue(confirmedUser.getEnabled());

        UserDetails userDetails = userService.loadUserByUsername(signUpDto.getEmail());
        assertNotNull(userDetails);
        assertEquals(signUpDto.getEmail(), userDetails.getUsername());
        assertTrue(passwordEncoder.matches("Test123!", userDetails.getPassword()));

        userService.deleteUser(confirmedUser.getId());

        assertFalse(userRepository.findById(confirmedUser.getId()).isPresent());
    }
    @Test
    public void testUserEditDetails() {

        SignUpDto signUpDto = new SignUpDto("Maria", "Byk", "test@example.com", "123-321-456","zxcasd" );
        UserModel user = userService.registerNewUser(signUpDto, response);

        UserRole userRole = userRoleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Collections.singleton(userRole));

        user.setFirstName("Alina");
        user.setLastName("Klon");
        user.setPhone("987-654-321");
        userService.saveEditUser(user);

        UserModel updatedUser = userRepository.findById(user.getId()).orElseThrow(AssertionError::new);
        assertThat(updatedUser.getFirstName()).isEqualTo("Alina");
        assertThat(updatedUser.getLastName()).isEqualTo("Klon");
        assertThat(updatedUser.getPhone()).isEqualTo("987-654-321");
    }

    @Test
    public void testUserResetPassword() {

        SignUpDto signUpDto = new SignUpDto("Jan", "Byk", "test2@example.com", "123-321-123","zxc" );
        UserModel user = userService.registerNewUser(signUpDto, response);

        UserRole userRole = userRoleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Collections.singleton(userRole));

        String resetStatus = notificationService.requestPasswordReset(user.getEmail());
        assertEquals("SUCCESS", resetStatus);

        VerificationToken resetTokenTest2 = verificationTokenRepository.findByUserModel(user)
                .orElseThrow(AssertionError::new);

        String newPassword = "NewPassword123!";
        String resetPasswordStatus = notificationService.resetPassword(resetTokenTest2.getToken(), newPassword);
        assertEquals("SUCCESS", resetPasswordStatus);

        UserModel updatedUser = userRepository.findById(user.getId()).orElseThrow(AssertionError::new);
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));
    }


}