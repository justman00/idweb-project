package com.university.platform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserRequestDto {

    @NotNull(message = "User should have first name")
    private String firstName;
    @NotNull(message = "User should have last name")
    private String lastName;
    @NotNull(message = "User should have email")
    private String email;
    @NotNull(message = "User should have password")
    private String password;
}
