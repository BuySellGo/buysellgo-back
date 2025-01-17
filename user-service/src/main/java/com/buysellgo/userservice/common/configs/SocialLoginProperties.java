package com.buysellgo.userservice.common.configs;

import com.buysellgo.userservice.domain.user.LoginType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import static com.buysellgo.userservice.util.CommonConstant.*;


@Component
@Getter
public class SocialLoginProperties {

    @Value("${sns.kakao.redirect-url}")
    private String kakaoRedirectUrl;

    @Value("${sns.google.redirect-url}")
    private String googleRedirectUrl;

    @Value("${sns.naver.redirect-url}")
    private String naverRedirectUrl;

    @Value("${sns.kakao.client-id}")
    private String kakaoClientId;

    @Value("${sns.google.client-id}")
    private String googleClientId;

    @Value("${sns.naver.client-id}")
    private String naverClientId;

    @Value("${sns.naver.client-secret}")
    private String naverClientSecret;

    @Value("${sns.google.client-secret}")
    private String googleClientSecret;

    @Value("${sns.kakao.callback-url}")
    private String kakaoCallbackUrl;

    @Value("${sns.google.callback-url}")
    private String googleCallbackUrl;

    @Value("${sns.naver.callback-url}")
    private String naverCallbackUrl;


    public String getRedirectUrl(LoginType provider) {
        switch (provider) {
            case KAKAO -> {
                return this.kakaoRedirectUrl;
            }
            case GOOGLE -> {
                return this.googleRedirectUrl;
            }
            case NAVER -> {
                return this.naverRedirectUrl;
            }
            default -> {
                return TYPE_NOT_SUPPORTED.getValue();
            }
        }
    }
}
