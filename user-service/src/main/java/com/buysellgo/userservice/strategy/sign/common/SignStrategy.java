package com.buysellgo.userservice.strategy.sign.common;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.service.dto.SignUpDto;

/**
 * Strategy 패턴의 전략 인터페이스
 * 회원가입과 탈퇴에 대한 기본 동작을 정의
 * 각 사용자 유형(판매자, 구매자 등)별로 이 인터페이스를 구현하여 구체적인 로직을 제공
 */
public interface SignStrategy<T> {
    /**
     * 회원가입 처리
     * @param signUpDto 회원가입 요청 데이터 (UserSignUpDto, SellerSignUpDto)
     * @return 회원가입 처리 결과
     */
    SignResult<T> signUp(SignUpDto signUpDto);

    /**
     * 회원 탈퇴 처리
     * @param token 사용자 인증 토큰
     * @return 탈퇴 처리 결과
     */
    SignResult<Void> withdraw(String token);

    /**
     * 현재 전략이 특정 역할을 지원하는지 확인
     * @param role 확인할 사용자 역할
     * @return 지원하는 경우 true, 아닌 경우 false
     */
    boolean supports(Role role);
}

