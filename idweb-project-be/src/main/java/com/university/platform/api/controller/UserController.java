package com.university.platform.api.controller;

import com.university.platform.api.dto.UserRequestDto;
import com.university.platform.api.dto.UserResponseDto;
import com.university.platform.api.exchange.Response;
import com.university.platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;

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

    @GetMapping
    public ResponseEntity<Response<UserResponseDto>> getUser(@RequestParam("accessToken") String accessToken) {
        return ok(Response.build(userService.getUserByToken(accessToken)));
    }
}
