package com.university.platform.service;

import com.university.platform.api.dto.UserRequestDto;
import com.university.platform.api.dto.UserResponseDto;
import com.university.platform.model.User;
import com.university.platform.repository.UserRepository;
import com.university.platform.utils.exceptions.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.university.platform.utils.exceptions.ExceptionType.USER_ALREADY_EXISTS;
import static com.university.platform.utils.exceptions.ExceptionType.USER_NOT_FOUND;
import static com.university.platform.utils.mappers.UserMapper.mapUserRequestDtoToUser;
import static com.university.platform.utils.mappers.UserMapper.mapUserToUserResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        User user = getUserByUsername(email);

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("user"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Transactional
    public UserResponseDto saveUser(final UserRequestDto userRequestDto) {
        final boolean userExists = userRepository.existsByEmail(userRequestDto.getEmail());
        final User user;

        if (!userExists) {
            user = mapUserRequestDtoToUser.apply(userRequestDto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);
            log.debug("User with [{}] email was saved.", user.getId());
        } else {
            log.debug("User with email {} already exists.", userRequestDto.getEmail());
            throw new PlatformException(USER_ALREADY_EXISTS);
        }

        return mapUserToUserResponseDto.apply(user);
    }

    public User getUserByUsername(final String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> {
                    log.warn("User with email [{}] was not found in the database", email);
                    return PlatformException.of(USER_NOT_FOUND, email);
                }
        );
    }

    public User getUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (user instanceof UserDetails) {
            email = ((UserDetails) user).getUsername();
        } else {
            email = user.toString();
        }

        return getUserByUsername(email);
    }
}
