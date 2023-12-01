package com.example.autodeal.user.service;

import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.VerificationToken;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.VerificationTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private JavaMailSender mailSender;

    @BeforeEach
    public void setUp() {
        mailSender = mock(JavaMailSender.class);
    }

    @Test
    public void testRegisterNewUser() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setFirstName("Jan");
        signUpDto.setLastName("Kot");
        signUpDto.setPhone("203-356-689");
        signUpDto.setEmail("newuser@example.com");
        signUpDto.setPassword("password");

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDto)))
                .andExpect(status().isOk());

        UserModel savedUser = userRepository.findByEmail("newuser@example.com").orElse(null);
        assertNotNull(savedUser);
        assertTrue(passwordEncoder.matches("password", savedUser.getPassword()));

        VerificationToken token = tokenRepository.findByUserModel(savedUser).orElse(null);
        assertNotNull(token);

        // Verify that an email was attempted to be sent
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
