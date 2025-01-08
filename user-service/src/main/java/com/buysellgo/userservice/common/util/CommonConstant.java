package com.buysellgo.userservice.common.util;

public enum CommonConstant {
    // 메시지 상수
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    TOKEN_OR_USER_NOT_FOUND("리프레시 토큰이 만료되었거나, 해당 사용자가 존재하지 않습니다."),
    PASSWORD_NOT_MATCHED("패스워드가 일치하지 않습니다."),
    REFRESH_TOKEN_DELETED("리프레시 토큰이 삭제되었습니다."),
    DTO_NOT_MATCHED("해당 dto를 지원하지 않습니다."),
    SELLER_CREATED("판매자 가입완료"),
    USER_CREATED("회원 가입완료"),
    USER_DELETED("회원 삭제완료"),
    SELLER_DELETED("판매자 삭제완료"),
    VALUE_DUPLICATED("중복된 값이 있습니다."),
    SAVE_FAILURE("저장 실패"),

    // 토큰 상수
    BEARER_PREFIX("Bearer "),
    ACCESS_TOKEN("accessToken"),
    
    // 응답 키값 상수
    EMAIL("email"),
    ROLE("role"),
    USER_VO("userVo"),
    SELLER_VO("sellerVo"),
    ADMIN_VO("adminVo"),
    FAILURE("failure"),


    // 숫자 상수
    KEEP_LOGIN_HOURS(168L),
    DEFAULT_HOURS(10L), ;

    private final Object value;

    <T> CommonConstant(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }
}
