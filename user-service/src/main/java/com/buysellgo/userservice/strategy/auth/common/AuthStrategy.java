package com.buysellgo.userservice.strategy.auth.common;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.strategy.auth.dto.AuthDto;
import java.util.Map;

/**
 * 인증 전략을 정의하는 인터페이스입니다.
 * 각 구현체는 JWT 생성, 갱신, 삭제 및 역할 지원 여부를 처리해야 합니다.
 *
 * @param <T> AuthResult에 반환되는 데이터의 타입입니다.
 */
public interface AuthStrategy<T extends Map<String, Object>> {

    /**
     * 주어진 인증 정보를 사용하여 JWT를 생성합니다.
     *
     * @param dto 인증 정보를 포함하는 AuthDto입니다.
     * @return JWT 생성 결과를 포함하는 AuthResult입니다.
     */
    AuthResult<T> createJwt(AuthDto dto);

    /**
     * 주어진 토큰을 사용하여 JWT를 갱신합니다.
     *
     * @param token 갱신할 JWT 토큰입니다.
     * @return JWT 갱신 결과를 포함하는 AuthResult입니다.
     */
    AuthResult<T> updateJwt(String token);

    /**
     * 주어진 토큰을 사용하여 JWT를 삭제합니다.
     *
     * @param token 삭제할 JWT 토큰입니다.
     * @return JWT 삭제 결과를 포함하는 AuthResult입니다.
     */
    AuthResult<T> deleteToken(String token);

    /**
     * 주어진 역할을 이 전략이 지원하는지 여부를 결정합니다.
     *
     * @param role 확인할 역할입니다.
     * @return 전략이 역할을 지원하면 true, 그렇지 않으면 false입니다.
     */
    boolean supports(Role role);
}
