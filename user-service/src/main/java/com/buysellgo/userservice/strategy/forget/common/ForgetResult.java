package com.buysellgo.userservice.strategy.forget.common;

public record ForgetResult<T>(
    boolean success,
    String message,
    T data
) {
    public static <T> ForgetResult<T> success(String message, T data) {
        return new ForgetResult<>(true, message, data);
    }

    public static <T> ForgetResult<T> fail(String message, T data) {
        return new ForgetResult<>(false, message, data);
    }
}
