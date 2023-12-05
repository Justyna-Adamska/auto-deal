package com.example.autodeal.user.mapper;

import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.dto.UserDto;
import com.example.autodeal.user.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void whenMappingNullUserModel_thenReturnsNull() {
        assertNull(userMapper.mapToUserDto(null));
    }

    @Test
    void whenMappingNullUserDto_thenReturnsNull() {
        assertNull(userMapper.mapToUserModel(null));
    }

    @Test
    void whenMappingValidUserModel_thenReturnsCorrectUserDto() {
        LocalDateTime now = LocalDateTime.now();
        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setFirstName("Jan");
        userModel.setLastName("Byk");
        userModel.setEmail("j.byk@example.com");
        userModel.setPhone("1234567890");
        userModel.setLastLoginDate(now);

        UserDto userDto = userMapper.mapToUserDto(userModel);

        assertEquals(userModel.getId(), userDto.getId().intValue());
        assertEquals(userModel.getFirstName(), userDto.getFirstName());
        assertEquals(userModel.getLastName(), userDto.getLastName());
        assertEquals(userModel.getEmail(), userDto.getEmail());
        assertEquals(userModel.getPhone(), userDto.getPhone());
        assertEquals(userModel.getLastLoginDate(), userDto.getLastLoginDate());
    }

    @Test
    void whenMappingValidUserDto_thenReturnsCorrectUserModel() {
        LocalDateTime now = LocalDateTime.now();
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("Jan");
        userDto.setLastName("Byk");
        userDto.setEmail("j.byk@example.com");
        userDto.setPhone("1234567890");
        userDto.setLastLoginDate(now);

        UserModel userModel = userMapper.mapToUserModel(userDto);

        assertEquals(userDto.getId().intValue(), userModel.getId());
        assertEquals(userDto.getFirstName(), userModel.getFirstName());
        assertEquals(userDto.getLastName(), userModel.getLastName());
        assertEquals(userDto.getEmail(), userModel.getEmail());
        assertEquals(userDto.getPhone(), userModel.getPhone());
        assertEquals(userDto.getLastLoginDate(), userModel.getLastLoginDate());
    }

    @Test
    void whenMappingUserModelWithMissingFields_thenReturnsUserDtoWithNulls() {
        UserModel userModel = new UserModel();
        userModel.setId(1);

        UserDto userDto = userMapper.mapToUserDto(userModel);

        assertEquals(userModel.getId(), userDto.getId().intValue());
        assertNull(userDto.getFirstName());
        assertNull(userDto.getLastName());
        assertNull(userDto.getEmail());
        assertNull(userDto.getPhone());
        assertNull(userDto.getLastLoginDate());
    }

    @Test
    void whenMappingValidSignUpDto_thenReturnsCorrectUserModel() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setFirstName("Jan");
        signUpDto.setLastName("Byk");
        signUpDto.setEmail("j.byk@example.com");
        signUpDto.setPhone("1234567890");

        UserModel userModel = userMapper.mapSignUpDtoToUserModel(signUpDto);

        assertEquals(signUpDto.getFirstName(), userModel.getFirstName());
        assertEquals(signUpDto.getLastName(), userModel.getLastName());
        assertEquals(signUpDto.getEmail(), userModel.getEmail());
        assertEquals(signUpDto.getPhone(), userModel.getPhone());
    }
    @Test
    void whenMappingUserModelWithNullFields_thenReturnsUserDtoWithNulls() {
        UserModel userModel = new UserModel();
        userModel.setId(1);

        UserDto userDto = userMapper.mapToUserDto(userModel);

        assertNotNull(userDto);
        assertEquals(userModel.getId(), userDto.getId().intValue());
        assertNull(userDto.getFirstName());
        assertNull(userDto.getLastName());
        assertNull(userDto.getEmail());
        assertNull(userDto.getPhone());
        assertNull(userDto.getLastLoginDate());
    }

    @Test
    void whenMappingUserDtoWithNullFields_thenReturnsUserModelWithNulls() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        UserModel userModel = userMapper.mapToUserModel(userDto);

        assertNotNull(userModel);
        assertEquals(userDto.getId().intValue(), userModel.getId());
        assertNull(userModel.getFirstName());
        assertNull(userModel.getLastName());
        assertNull(userModel.getEmail());
        assertNull(userModel.getPhone());
        assertNull(userModel.getLastLoginDate());
    }
    @Test
    void whenMappingUserModelWithInvalidEmail_thenReturnsUserDtoWithInvalidEmail() {
        UserModel userModel = new UserModel();
        userModel.setId(1);
        userModel.setEmail("invalid-email");

        UserDto userDto = userMapper.mapToUserDto(userModel);

        assertNotNull(userDto);
        assertEquals("invalid-email", userDto.getEmail());
    }

    @Test
    void whenMappingUserDtoWithInvalidPhone_thenReturnsUserModelWithInvalidPhone() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPhone("invalid-phone-number");

        UserModel userModel = userMapper.mapToUserModel(userDto);

        assertNotNull(userModel);
        assertEquals("invalid-phone-number", userModel.getPhone());
    }
}
