package com.buysellgo.userservice.service.dto;

import java.util.Map;   

public record ServiceRes<T extends Map<String, Object>>(
    boolean success,
    String message,
    T data
) {
    public static <T extends Map<String, Object>> ServiceRes<T> success(String message, T data) {
        return new ServiceRes<>(true, message, data);
    }

    public static <T extends Map<String, Object>> ServiceRes<T> fail(String message, T data) {
        return new ServiceRes<>(false, message, data);
    }
}
