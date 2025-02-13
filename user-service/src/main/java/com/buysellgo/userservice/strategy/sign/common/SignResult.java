package com.buysellgo.userservice.strategy.sign.common;

public record SignResult<T>(
        boolean success,
        String message,
        T data
) {
    public static <T> SignResult<T> success(String message, T data) {
        return new SignResult<>(true, message,data);
    }

    public static <T> SignResult<T> fail(String message, T data) {
        return new SignResult<>(false, message,data);
    }
}
