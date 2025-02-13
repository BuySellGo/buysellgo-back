package com.buysellgo.userservice.strategy.forget.common;

import java.util.Map;

import com.buysellgo.userservice.common.entity.Role;

/**
 * ForgetStrategy 인터페이스는 사용자 �정 복구 전략을 정의합니다.
 * 이 인터페이스는 이메일 검증과 비밀번호 초기화 기능을 제공합니다.
 *
 * @param <T> 결과 데이터의 타입을 정의합니다.
 */
public interface ForgetStrategy<T extends Map<String, Object>> {

    /**
     * 사용자의 이메일이 가입된 이메일인지 검증하는 메서드입니다.
     *
     * @param email 검증할 사용자의 이메일 주소
     * @return ForgetResult 객체로, 이메일 검증 결과를 포함합니다.
     */
    ForgetResult<T> forgetEmail(String email);

    /**
     * 사용자의 비밀번호를 초기화하고, 초기화된 비밀번호를 이메일로 전송하는 메서드입니다.
     *
     * @param email 비밀번호를 초기화할 사용자의 이메일 주소
     * @return ForgetResult 객체로, 비밀번호 초기화 결과를 포함합니다.
     */
    ForgetResult<T> forgetPassword(String email);

    /**
     * 주어진 역할이 이 전략을 지원하는지 여부를 확인하는 메서드입니다.
     *
     * @param role 확인할 사용자의 역할
     * @return 이 전략이 주어진 역할을 지원하면 true, 그렇지 않으면 false를 반환합니다.
     */
    boolean supports(Role role);
}
