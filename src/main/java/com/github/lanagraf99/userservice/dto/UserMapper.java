package com.github.lanagraf99.userservice.dto;

import com.github.lanagraf99.userservice.model.User;

public class UserMapper {

    public static UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    public static User toEntity(UserRequestDto dto) {
        return new User(dto.getName(), dto.getEmail(), dto.getAge());
    }
}
