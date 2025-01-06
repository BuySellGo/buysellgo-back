package com.buysellgo.userservice.user.controller.dto;

import com.buysellgo.userservice.common.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.RequestHeader;

public record JwtUpdateReq(
        @Schema(title = "이메일", example = "test@test.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "이메일은 필수 입니다.")
        @Pattern(
                regexp = "^$|^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$",
                message = "올바른 이메일 형식이 아닙니다."
        )
        @RequestHeader("X-Email")
        String email,

        @Schema(title = "액세스 토큰", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "액세스 토큰은 필수 입니다.")
        @RequestHeader("Authorization")
        String accessToken,

        @Schema(
                title = "사용자 역할",
                example = "USER",
                description = "사용자 역할 (USER: 일반 사용자, SELLER: 판매자, ADMIN: 관리자)",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "사용자 역할은 필수 입니다.")
        @RequestHeader("X-Role")
        Role role
) {}
