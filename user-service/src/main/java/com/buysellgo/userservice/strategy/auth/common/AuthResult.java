package com.buysellgo.userservice.strategy.auth.common;

public record AuthResult<T>(
    boolean success,
    String message,
    T data
) {
    public static <T> AuthResult<T> success(String message, T data) {
        return new AuthResult<>(true, message, data);
    }

    public static <T> AuthResult<T> fail(String message) {
        return new AuthResult<>(false, message, null);
    }
}