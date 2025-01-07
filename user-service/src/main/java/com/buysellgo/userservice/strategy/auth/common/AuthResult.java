package com.buysellgo.userservice.strategy.auth.common;

public record AuthResult<T>(
    boolean success,
    String errorMessage,
    T data
) {
    public static <T> AuthResult<T> success(T data) {
        return new AuthResult<>(true, null, data);
    }

    public static <T> AuthResult<T> failure(String errorMessage) {
        return new AuthResult<>(false, errorMessage, null);
    }
}
