package com.university.platform.api.controller;

import com.university.platform.api.dto.UserRequestDto;
import com.university.platform.api.dto.UserResponseDto;
import com.university.platform.api.exchange.Response;
import com.university.platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Response<UserResponseDto>> saveUser(@RequestBody @Valid UserRequestDto userRequestDto,
            Errors validationErrors) {

        if (validationErrors.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(validationErrors.getFieldError()).getDefaultMessage());
        }

        return ResponseEntity.status(CREATED).body(Response.build(userService.saveUser(userRequestDto)));
    }
}
