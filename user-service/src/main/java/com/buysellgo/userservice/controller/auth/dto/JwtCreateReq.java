package com.buysellgo.userservice.controller.auth.dto;

import com.buysellgo.userservice.common.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record JwtCreateReq(
        @Schema(title = "이메일", example = "test@test.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "이메일은 필수 입니다.")
        @Pattern(
                regexp = "^$|^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$",
                message = "올바른 이메일 형식이 아닙니다."
        )
        String email,

        @Schema(title = "비밀번호", example = "test1234!", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "비밀번호는 필수 입니다.")
        @Pattern(
                regexp = "^$|^(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=\\S+$).{8,}$",
                message = "비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."
        )
        String password,

        @Schema(
                title = "자동 로그인",
                example = "ACTIVE",
                description = "자동 로그인 사용 여부 (ACTIVE: 사용, INACTIVE: 미사용)",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "자동 로그인 사용 여부는 필수 입니다.")
        KeepLogin keepLogin,

        @Schema(
                title = "사용자 역할",
                example = "USER",
                description = "사용자 역할 (USER: 일반 사용자, SELLER: 판매자, ADMIN: 관리자)",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "사용자 역할은 필수 입니다.")
        Role role
) {}
