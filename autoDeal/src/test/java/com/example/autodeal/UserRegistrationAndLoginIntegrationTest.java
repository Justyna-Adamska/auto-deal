package com.example.autodeal;

import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.VerificationToken;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.VerificationTokenRepository;
import com.example.autodeal.user.service.UserService;
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
import jakarta.servlet.http.HttpServletResponse;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserRegistrationAndLoginIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private HttpServletResponse response;

    @BeforeEach
    public void setup() {

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
    }

    @AfterEach
    public void cleanup() {
        verificationTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testUserRegistrationAndLogin() {

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
    }
}
