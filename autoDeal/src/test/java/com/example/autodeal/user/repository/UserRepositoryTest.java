package com.example.autodeal.user.repository;

import com.example.autodeal.user.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserModel user = new UserModel();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("1234567890");
        user.setEmail("existinguser@example.com");
        user.setPassword("password");
        userRepository.save(user);
    }



    @Test
    void whenFindByEmail_thenReturnUser() {
        // When
        Optional<UserModel> foundUser = userRepository.findByEmail("existinguser@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("existinguser@example.com");
    }

    @Test
    void whenFindByEmail_withNonExistingEmail_thenReturnEmpty() {
        // When
        Optional<UserModel> foundUser = userRepository.findByEmail("nonexistinguser@example.com");

        // Then
        assertThat(foundUser).isNotPresent();
    }

}