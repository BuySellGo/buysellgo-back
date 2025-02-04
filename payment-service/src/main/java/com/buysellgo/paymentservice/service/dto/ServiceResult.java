package com.buysellgo.paymentservice.service.dto;

public record ServiceResult<T>(
    boolean success,
    String message,
    T data
) {
    public static <T> ServiceResult<T> success(String message, T data) {    
        return new ServiceResult<>(true, message, data);
    }

    public static <T> ServiceResult<T> fail(String message, T data) {
        return new ServiceResult<>(false, message, data);
    }
}
