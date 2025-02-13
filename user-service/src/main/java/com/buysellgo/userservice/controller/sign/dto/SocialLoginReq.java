package com.buysellgo.userservice.controller.sign.dto;

import com.buysellgo.userservice.domain.user.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


public record SocialLoginReq (
    @Schema(title = "로그인 제공자", example = "GOOGLE", description = "로그인 제공자 (COMMON, KAKAO, GOOGLE, NAVER)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "로그인 제공자는 필수 입니다.")
    LoginType provider
){}
    