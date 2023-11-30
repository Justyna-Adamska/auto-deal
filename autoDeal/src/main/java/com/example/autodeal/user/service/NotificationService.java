package com.example.autodeal.user.service;

import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.VerificationToken;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import java.time.temporal.ChronoUnit;
import java.util.UUID;
@Slf4j
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    public NotificationService(UserRepository userRepository,
                               JavaMailSender mailSender,
                               PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public void sendActivationEmail(String email, String activationToken) {
        try {
            String activationUrl = "http://auto-deal.com/activate-account?token=" + activationToken;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Account Activation");
            message.setText("To activate your account, please click on the following link: " + activationUrl);
            mailSender.send(message);
        } catch (MailSendException e) {
            logger.error("Error sending activation email to {}: {}", email, e.getMessage());
        }
    }

    public boolean requestPasswordReset(String email) {
        UserModel user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            String resetToken = UUID.randomUUID().toString();

            LocalDateTime localDateTime = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
            Date expiryDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken(resetToken);
            verificationToken.setUserModel(user);
            verificationToken.setExpiryDate(expiryDate);
            verificationTokenRepository.save(verificationToken);

            sendResetEmail(email, resetToken);
            return true;
        }
        return false;
    }


    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    private void sendResetEmail(String email, String resetToken) {
        String resetUrl = "http://auto-deal.com/reset-password?token=" + resetToken;


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset");
        message.setText("To reset your password, please click on the following link: " + resetUrl);
        mailSender.send(message);
    }

    public boolean resetPassword(String token, String newPassword) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElse(null);
        if (verificationToken != null && verificationToken.getExpiryDate().after(new Date())) {
            UserModel user = verificationToken.getUserModel();
            if (user != null) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                verificationTokenRepository.delete(verificationToken);
                return true;
            }
        }
        return false;
    }

}
