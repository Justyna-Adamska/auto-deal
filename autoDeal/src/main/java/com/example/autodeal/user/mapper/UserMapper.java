package com.example.autodeal.user.mapper;

import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.dto.UserDto;
import com.example.autodeal.user.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        if (userModels == null) {
            return null;
        }
        List<UserDto> userDtos = new ArrayList<>();
        for (UserModel userModel : userModels) {
            userDtos.add(mapToUserDto(userModel));
        }
        return userDtos;
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

        return userModel;
    }

    public List<UserModel> mapToUserModelList(List<UserDto> userDtos) {
        if (userDtos == null) {
            return null;
        }
        return userDtos.stream()
                .map(this::mapToUserModel)
                .collect(Collectors.toList());
    }
}
