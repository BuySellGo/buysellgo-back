package com.buysellgo.userservice.strategy.sign.common;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.strategy.sign.dto.ActivateDto;
import com.buysellgo.userservice.strategy.sign.dto.DuplicateDto;
import com.buysellgo.userservice.strategy.sign.dto.SignUpDto;

import java.util.Map;

/**
 * 회원 가입 및 관련 작업을 정의하는 인터페이스입니다.
 * 각 구현체는 회원 가입, 탈퇴, 활성화, 비활성화, 중복 확인 및 소셜 회원 가입을 처리해야 합니다.
 *
 * @param <T> SignResult에 반환되는 데이터의 타입입니다.
 */
public interface SignStrategy<T extends Map<String, Object>> {

    /**
     * 주어진 정보를 사용하여 회원 가입을 처리합니다.
     *
     * @param dto 회원 가입 정보를 포함하는 SignUpDto입니다.
     * @return 회원 가입 결과를 포함하는 SignResult입니다.
     */
    SignResult<T> signUp(SignUpDto dto);

    /**
     * 주어진 토큰을 사용하여 회원 탈퇴를 처리합니다.
     *
     * @param token 탈퇴할 회원의 토큰입니다.
     * @return 회원 탈퇴 결과를 포함하는 SignResult입니다.
     */
    SignResult<T> withdraw(String token);

    /**
     * 주어진 정보를 사용하여 계정을 활성화합니다.
     *
     * @param dto 계정 활성화 정보를 포함하는 ActivateDto입니다.
     * @return 계정 활성화 결과를 포함하는 SignResult입니다.
     */
    SignResult<T> activate(ActivateDto dto);

    /**
     * 주어진 정보를 사용하여 계정을 비활성화합니다.
     *
     * @param dto 계정 비활성화 정보를 포함하는 ActivateDto입니다.
     * @return 계정 비활성화 결과를 포함하는 SignResult입니다.
     */
    SignResult<T> deactivate(ActivateDto dto);

    /**
     * 주어진 정보를 사용하여 계정 중복 여부를 확인합니다.
     *
     * @param dto 계정 중복 확인 정보를 포함하는 DuplicateDto입니다.
     * @return 계정 중복 여부를 포함하는 SignResult입니다.
     */
    SignResult<T> duplicate(DuplicateDto dto);

    /**
     * 소셜 회원 가입을 처리합니다.
     *
     * @return 소셜 회원 가입 결과를 포함하는 SignResult입니다.
     */
    SignResult<T> socialSignUp(String email, String provider);

    /**
     * 전략이 주어진 역할을 지원하는지 여부를 결정합니다.
     *
     * @param role 확인할 역할입니다.
     * @return 전략이 역할을 지원하면 true, 그렇지 않으면 false입니다.
     */
    boolean supports(Role role);
}

