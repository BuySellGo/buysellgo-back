package com.buysellgo.productservice.strategy.common;

public record ProductResult<T>(
    boolean success,
    String message,
    T data
) { 
    public static <T> ProductResult<T> success(String message, T data) {
        return new ProductResult<>(true, message, data);
    }

    public static <T> ProductResult<T> fail(String message, T data) {
        return new ProductResult<>(false, message, data);
    }
}
