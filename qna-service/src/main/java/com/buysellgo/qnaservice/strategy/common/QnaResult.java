package com.buysellgo.qnaservice.strategy.common;
    
public record QnaResult<T>(
    boolean success,
    String message,
    T data
) {
    public static <T> QnaResult<T> success(String message, T data) {
        return new QnaResult<>(true, message, data);
    }

    public static <T> QnaResult<T> fail(String message, T data) {
        return new QnaResult<>(false, message, data);
    }
}
