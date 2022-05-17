package com.university.platform.api.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response<V> {

    private V data;

    private Error error;

    public static <V> Response<V> build(V data) {
        return new Response<>(data, null);
    }

    public static Response<?> build(Error error) {
        return new Response<>(null, error);
    }

    public static <V> Response<V> build(V data, Error error) {
        return new Response<>(data, error);
    }
}