package com.example.autodeal.user.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.VerificationToken;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

class NotificationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;


    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendActivationEmailTest() {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        notificationService.sendActivationEmail(email, token);

        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        assertEquals(email, capturedMessage.getTo()[0]);
        assertEquals("Account Activation", capturedMessage.getSubject());
        assertTrue(capturedMessage.getText().contains(token));
    }

    @Test
    void requestPasswordReset_UserExists() {
        String email = "test@example.com";
        UserModel mockUser = new UserModel();
        mockUser.setEmail(email);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        boolean result = notificationService.requestPasswordReset(email);

        assertTrue(result);
        verify(verificationTokenRepository, times(1)).save(any(VerificationToken.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    @Test
    void requestPasswordReset_UserDoesNotExist() {

        String email = "test@example.com";
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        boolean result = notificationService.requestPasswordReset(email);

        assertFalse(result);
        verify(userRepository, never()).save(any(UserModel.class));
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void resetPassword_ValidToken() {
        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword";
        UserModel mockUser = new UserModel();
        VerificationToken mockVerificationToken = new VerificationToken();
        mockVerificationToken.setToken(token);
        mockVerificationToken.setUserModel(mockUser);
        mockVerificationToken.setExpiryDate(new Date(System.currentTimeMillis() + 100000)); // Ustawienie daty na przyszłość

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(mockVerificationToken));

        boolean result = notificationService.resetPassword(token, newPassword);

        assertTrue(result);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(mockUser);
    }


    @Test
    void resetPassword_InvalidToken() {
        String token = UUID.randomUUID().toString();
        when(userRepository.findByResetToken(token)).thenReturn(Optional.empty());

        boolean result = notificationService.resetPassword(token, "newPassword");

        assertFalse(result);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    void sendActivationEmail_ThrowsException() {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();

        doThrow(new MailSendException("Error sending email")).when(mailSender).send(any(SimpleMailMessage.class));

        notificationService.sendActivationEmail(email, token);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

    }


    @Test
    void resetPassword_ValidToken_NotExpired() {


        String token = UUID.randomUUID().toString();
        String newPassword = "newPassword";
        UserModel mockUser = new UserModel();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserModel(mockUser);

        LocalDateTime futureDateTime = LocalDateTime.now().plusDays(1);
        Date futureDate = Date.from(futureDateTime.atZone(ZoneId.systemDefault()).toInstant());
        verificationToken.setExpiryDate(futureDate);

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        boolean result = notificationService.resetPassword(token, newPassword);

        assertTrue(result);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void resetPassword_ValidToken_Expired() {
        String token = UUID.randomUUID().toString();
        UserModel mockUser = new UserModel();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserModel(mockUser);

        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);
        Date pastDate = Date.from(pastDateTime.atZone(ZoneId.systemDefault()).toInstant());
        verificationToken.setExpiryDate(pastDate);


        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        boolean result = notificationService.resetPassword(token, "newPassword");

        assertFalse(result);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(UserModel.class));
    }
}