package com.university.platform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
