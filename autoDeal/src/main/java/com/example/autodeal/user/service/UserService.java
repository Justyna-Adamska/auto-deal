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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, NotificationService notificationService, VerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
        this.verificationTokenRepository = tokenRepository;
    }

    public void addUser(UserModel user) {
        userRepository.save(user);
    }

    public List<UserModel> findAllUsers() {
        return userRepository.findAll();
    }

    public UserModel findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Could not find user by id"));
    }

    public UserModel findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
    }

    public void saveEditUser(UserModel editUser) {
        userRepository.save(editUser);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = findUserByEmail(email);
        Boolean isActivated = Optional.ofNullable(user.getEnabled()).orElse(false);

        // if (!isActivated) {
        //   throw new UsernameNotFoundException("User not activated");
        //}

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public UserModel registerNewUser(SignUpDto signUpDto) {

        userRepository.findByEmail(signUpDto.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("Registration failed, please try again.");
        });

        UserModel newUser = new UserModel();
        newUser.setEmail(signUpDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(signUpDto.getPassword())); // Zakodowanie hasła

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

        return savedUser;
    }


    public void confirmUserRegistration(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        UserModel user = verificationToken.getUserModel();
        user.setEnabled(true);
        userRepository.save(user);
    }

}