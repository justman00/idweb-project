package com.university.platform.api.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.university.platform.api.exchange.Error;
import com.university.platform.api.exchange.Response;
import com.university.platform.model.User;
import com.university.platform.service.UserService;
import com.university.platform.utils.exceptions.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.university.platform.utils.exceptions.ExceptionType.INVALID_AUTHORIZATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final UserService userService;

    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();

                User user = userService.getUserByUsername(username);

                String accessToken = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", Collections.singletonList("ROLE_USER"))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();

                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);

                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e) {
                response.setStatus(FORBIDDEN.value());
                ObjectMapper mapper = new ObjectMapper();

                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                Error er = new Error(FORBIDDEN, "Invalid authorization token", LocalDateTime.now());
                Response<?> message = new Response<>();
                message.setError(er);

                response.setContentType(APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getOutputStream(), message);

                log.error("Unable to login in: {}", e.getMessage());
                throw new PlatformException(INVALID_AUTHORIZATION);
            }
        } else {
            throw new PlatformException(INVALID_AUTHORIZATION);
        }
    }
}
