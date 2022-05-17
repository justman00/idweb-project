package com.university.platform.api.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Error<V> {

    private V customStatus;

    private String message;

    private LocalDateTime timestamp;

    public static <V> Error<V> build(V status, String message, LocalDateTime timestamp) {
        return new Error<>(status, message, timestamp);
    }
}