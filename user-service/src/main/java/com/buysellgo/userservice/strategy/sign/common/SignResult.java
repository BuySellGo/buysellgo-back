package com.buysellgo.userservice.strategy.sign.common;

/**
 * 회원가입/탈퇴 작업의 결과를 나타내는 불변 객체
 * Record를 사용하여 결과 데이터를 간단하게 캡슐화
 */
public record SignResult<T>(
    boolean success,
    String errorMessage,
    T data
) {
    /**
     * 성공 결과를 생성하는 팩토리 메서드
     * @param data 성공 시 반환할 데이터
     * @return 성공 결과 객체
     */
    public static <T> SignResult<T> success(T data) {
        return new SignResult<>(true, null, data);
    }

    /**
     * 실패 결과를 생성하는 팩토리 메서드
     * @param errorMessage 실패 사유를 설명하는 메시지
     * @return 실패 결과 객체
     */
    public static <T> SignResult<T> failure(String errorMessage) {
        return new SignResult<>(false, errorMessage, null);
    }
}
