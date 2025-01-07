package com.buysellgo.userservice.common.util;

public enum CommonConstant {
    // 메시지 상수
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    TOKEN_OR_USER_NOT_FOUND("리프레시 토큰이 만료되었거나, 해당 사용자가 존재하지 않습니다."),
    
    // 토큰 상수
    BEARER_PREFIX("Bearer "),
    ACCESS_TOKEN("accessToken"),
    
    // 응답 키값 상수
    EMAIL("email"),
    ROLE("role"),
    FAILURE("failure");

    private final String value;

    CommonConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
