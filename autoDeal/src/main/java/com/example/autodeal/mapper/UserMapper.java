package com.example.autodeal.mapper;

import com.example.autodeal.dto.UserDto;
import com.example.autodeal.model.UserModel;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {

    public static UserDto mapToUserDto(UserModel userModel) {
        return UserDto.builder()
                .id(Long.valueOf(userModel.getId()))
                .firstName(userModel.getFirstName())
                .lastName(userModel.getLastName())
                .email(userModel.getEmail())
                .phone(userModel.getPhone())
                .lastLoginDate(userModel.getLastLoginDate())
                .build();
    }

    public static List<UserDto> mapToUserDtoList(List<UserModel> userModels) {
        List<UserDto> userList = new ArrayList<>();
        for (UserModel userModel : userModels) {
            userList.add(mapToUserDto(userModel));
        }
        return userList;
    }
}


