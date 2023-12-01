package com.example.autodeal.user.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.example.autodeal.user.model.UserModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SignUpDtoTest {

    @Test
    void testSignUpDto() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String phone = "1234567890";
        String password = "password123";

        SignUpDto signUpDto = new SignUpDto(firstName, lastName, email, phone, password);

        assertEquals(firstName, signUpDto.getFirstName());
        assertEquals(lastName, signUpDto.getLastName());
        assertEquals(email, signUpDto.getEmail());
        assertEquals(phone, signUpDto.getPhone());
        assertEquals(password, signUpDto.getPassword());

        String newFirstName = "Jane";
        signUpDto.setFirstName(newFirstName);
        assertEquals(newFirstName, signUpDto.getFirstName());

        SignUpDto anotherSignUpDto = new SignUpDto(newFirstName, lastName, email, phone, password);
        assertEquals(signUpDto.toString(), anotherSignUpDto.toString());
        assertEquals(signUpDto, anotherSignUpDto);
        assertEquals(signUpDto.hashCode(), anotherSignUpDto.hashCode());
    }
    @Test
    void testSignUpDtoMappingToUserModel() {
        // Create a SignUpDto instance with test data
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String phone = "1234567890";
        String password = "password123";

        SignUpDto signUpDto = new SignUpDto(firstName, lastName, email, phone, password);

        // Manually map SignUpDto to UserModel
        UserModel userModel = new UserModel();
        userModel.setFirstName(signUpDto.getFirstName());
        userModel.setLastName(signUpDto.getLastName());
        userModel.setEmail(signUpDto.getEmail());
        userModel.setPhone(signUpDto.getPhone());
        userModel.setPassword(signUpDto.getPassword()); // Note: In a real scenario, the password should be encoded

        // Assert that UserModel fields match SignUpDto fields
        assertEquals(signUpDto.getFirstName(), userModel.getFirstName());
        assertEquals(signUpDto.getLastName(), userModel.getLastName());
        assertEquals(signUpDto.getEmail(), userModel.getEmail());
        assertEquals(signUpDto.getPhone(), userModel.getPhone());
        assertEquals(signUpDto.getPassword(), userModel.getPassword()); // Password comparison is direct here for simplicity
    }
}