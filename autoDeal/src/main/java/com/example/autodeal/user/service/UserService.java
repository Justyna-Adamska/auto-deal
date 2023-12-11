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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.autodeal.user.mapper.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder, NotificationService notificationService,
                       VerificationTokenRepository tokenRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
        this.verificationTokenRepository = tokenRepository;
        this.userMapper = userMapper;
    }

    public void addUser(UserModel user) {
        userRepository.save(user);
    }

    public List<UserModel> findAllUsers() {
        return userRepository.findAll();
    }

    public UserModel findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Could not find user by id"));
    }

    public UserModel findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
    }

    public void saveEditUser(UserModel editUser) {
        userRepository.save(editUser);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " does not exist");
        }

        userRepository.deleteById(id);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = findUserByEmail(email);
        Boolean isActivated = Optional.ofNullable(user.getEnabled()).orElse(false);

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public UserModel registerNewUser(SignUpDto signUpDto, HttpServletResponse response) {
        userRepository.findByEmail(signUpDto.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("Registration failed, please try again.");
        });

        UserModel newUser = userMapper.mapSignUpDtoToUserModel(signUpDto);
        newUser.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        UserRole defaultRole = userRoleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found."));
        newUser.setRoles(Set.of(defaultRole));

        UserModel savedUser = userRepository.save(newUser);

        String activationToken = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(activationToken);
        verificationToken.setUserModel(savedUser);
        verificationToken.setExpiryDate(new Date());
        verificationTokenRepository.save(verificationToken);

        notificationService.sendActivationEmail(newUser.getEmail(), activationToken);
        createAndAddSessionCookie(response);

        return savedUser;
    }

    private void createAndAddSessionCookie(HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        Cookie sessionCookie = new Cookie("session-id", sessionId);
        sessionCookie.setMaxAge(7 * 24 * 60 * 60); // Ciasteczko ważne przez 7 dni
        sessionCookie.setHttpOnly(true); // Zabezpieczenie przed dostępem przez JavaScript
        sessionCookie.setPath("/"); // Dostępne dla całej domeny
        response.addCookie(sessionCookie);
    }


    public void confirmUserRegistration(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        UserModel user = verificationToken.getUserModel();
        user.setEnabled(true);
        userRepository.save(user);
    }
}
