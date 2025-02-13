package com.buysellgo.userservice.strategy.social.common;

import org.springframework.util.MultiValueMap;
import java.util.Map;

/**
 * 소셜 로그인 전략을 정의하는 인터페이스입니다.
 * 각 구현체는 특정 제공자에 대한 OAuth 2.0 흐름을 처리해야 합니다.
 *
 * @param <T> SocialLoginResult에 반환되는 데이터의 타입입니다.
 */
public interface SocialLoginStrategy<T extends Map<String, Object>> {

    /**
     * 제공된 인증 코드를 사용하여 소셜 로그인 프로세스를 실행합니다.
     *
     * @param code 소셜 제공자로부터 받은 인증 코드입니다.
     * @return 사용자 정보 또는 오류 메시지를 포함하는 SocialLoginResult입니다.
     */
    SocialLoginResult<T> execute(String code);

    /**
     * 액세스 토큰 요청에 필요한 파라미터를 생성합니다.
     *
     * @param code 소셜 제공자로부터 받은 인증 코드입니다.
     * @return 토큰 요청 파라미터를 포함하는 MultiValueMap입니다.
     */
    MultiValueMap<String, String> createTokenParams(String code);

    /**
     * 제공된 파라미터를 사용하여 소셜 제공자로부터 액세스 토큰을 요청합니다.
     *
     * @param tokenParams 토큰 요청에 필요한 파라미터입니다.
     * @return 액세스 토큰 또는 오류 메시지를 포함하는 SocialLoginResult입니다.
     */
    SocialLoginResult<T> requestAccessToken(MultiValueMap<String, String> tokenParams);

    /**
     * 토큰 결과에서 액세스 토큰을 추출합니다.
     *
     * @param tokenResult 액세스 토큰 데이터를 포함하는 결과입니다.
     * @return 액세스 토큰 문자열입니다.
     */
    String extractAccessToken(SocialLoginResult<T> tokenResult);

    /**
     * 액세스 토큰을 사용하여 소셜 제공자로부터 사용자 정보를 요청합니다.
     *
     * @param accessToken 요청을 인증하는 데 사용되는 액세스 토큰입니다.
     * @return 사용자 정보 또는 오류 메시지를 포함하는 SocialLoginResult입니다.
     */
    SocialLoginResult<T> requestUserInfo(String accessToken);

    /**
     * 전략이 주어진 소셜 제공자를 지원하는지 여부를 결정합니다.
     *
     * @param provider 소셜 제공자의 이름입니다.
     * @return 전략이 제공자를 지원하면 true, 그렇지 않으면 false입니다.
     */
    Boolean support(String provider);
}


