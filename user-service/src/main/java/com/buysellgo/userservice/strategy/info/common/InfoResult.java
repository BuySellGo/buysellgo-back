package com.buysellgo.userservice.strategy.info.common;

public record InfoResult<T>(
    boolean success,
    String message,
    T data
) {
    public static <T> InfoResult<T> success(String message, T data) {
        return new InfoResult<>(true, message, data);
    }

    public static <T> InfoResult<T> fail(String message, T data) {
        return new InfoResult<>(false, message, data);
    }
}
