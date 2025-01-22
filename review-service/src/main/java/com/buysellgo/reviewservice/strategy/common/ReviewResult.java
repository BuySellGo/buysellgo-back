package com.buysellgo.reviewservice.strategy.common;        

public record ReviewResult<T>(
    boolean success,
    String message,
    T data
) {
    public static <T> ReviewResult<T> success(String message, T data) {
        return new ReviewResult<>(true, message, data);
    }

    public static <T> ReviewResult<T> fail(String message, T data) {
        return new ReviewResult<>(false, message, data);
    }
}

