package com.university.platform.utils;

import com.university.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

//@NoArgsConstructor(access = PRIVATE)
@RequiredArgsConstructor
public class ContextHolderUtils {

    private final UserRepository userRepository;

    public static Long getUserId() {

        String s = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return null;
    }


}
