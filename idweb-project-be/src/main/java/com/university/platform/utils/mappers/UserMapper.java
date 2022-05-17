package com.university.platform.utils.mappers;


import com.university.platform.api.dto.UserRequestDto;
import com.university.platform.api.dto.UserResponseDto;
import com.university.platform.model.User;
import lombok.NoArgsConstructor;

import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class UserMapper {

    public static final Function<UserRequestDto, User> mapUserRequestDtoToUser = userRequestDto ->
            User.builder()
                    .email(userRequestDto.getEmail())
                    .firstName(userRequestDto.getFirstName())
                    .lastName(userRequestDto.getLastName())
                    .password(userRequestDto.getPassword())
                    .build();

    public static final Function<User, UserResponseDto> mapUserToUserResponseDto = user ->
            UserResponseDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .build();
}