package com.example.autodeal.user.mapper;

import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.dto.UserDto;
import com.example.autodeal.user.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto mapToUserDto(UserModel userModel) {
        if (userModel == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(Long.valueOf(userModel.getId()));
        userDto.setFirstName(userModel.getFirstName());
        userDto.setLastName(userModel.getLastName());
        userDto.setEmail(userModel.getEmail());
        userDto.setPhone(userModel.getPhone());
        userDto.setLastLoginDate(userModel.getLastLoginDate());

        return userDto;
    }

    public List<UserDto> mapToUserDtoList(List<UserModel> userModels) {
        return userModels == null ? null : userModels.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserModel mapSignUpDtoToUserModel(SignUpDto signUpDto) {
        if (signUpDto == null) {
            return null;
        }

        UserModel userModel = new UserModel();
        userModel.setFirstName(signUpDto.getFirstName());
        userModel.setLastName(signUpDto.getLastName());
        userModel.setEmail(signUpDto.getEmail());
        userModel.setPhone(signUpDto.getPhone());
        // Remember to handle password encryption elsewhere as needed

        return userModel;
    }

    public UserModel mapToUserModel(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        UserModel userModel = new UserModel();
        userModel.setId(userDto.getId() != null ? userDto.getId().intValue() : null);
        userModel.setFirstName(userDto.getFirstName());
        userModel.setLastName(userDto.getLastName());
        userModel.setEmail(userDto.getEmail());
        userModel.setPhone(userDto.getPhone());
        userModel.setLastLoginDate(userDto.getLastLoginDate());
        // Handle additional fields like enabled, roles etc. as needed

        return userModel;
    }

    public List<UserModel> mapToUserModelList(List<UserDto> userDtos) {
        return userDtos == null ? null : userDtos.stream()
                .map(this::mapToUserModel)
                .collect(Collectors.toList());
    }
}
