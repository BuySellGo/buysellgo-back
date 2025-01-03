package com.buysellgo.userservice.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.AssertTrue;

public record UserCreateReq(
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
        @Schema(title = "닉네임", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "닉네임은 필수 입니다.")
        String username,
        @Schema(title = "휴대폰번호", example = "010-1234-5678", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "휴대폰 번호는 필수 입니다.")
        @Pattern(
                regexp = "^$|^01[0-1679]-\\d{3,4}-\\d{4}$",
                message = "올바른 휴대폰 번호 형식이 아닙니다."
        )
        String phone,
        @Schema(
                title = "이메일 인증 여부",
                example = "true",
                description = "이메일 인증 완료 여부",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "이메일 인증 여부는 필수 입니다.")
        @AssertTrue(message = "이메일 인증이 필요합니다.")
        Boolean emailCertified,

        @Schema(
                title = "개인정보 수집 및 이용 동의",
                example = "true",
                description = "개인정보 수집 및 이용에 대한 동의 여부",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "개인정보 수집 및 이용 동의는 필수 입니다.")
        @AssertTrue(message = "개인정보 수집 및 이용에 동의해야 합니다.")
        Boolean agreePICU,

        @Schema(
                title = "이메일 수신 동의",
                example = "true",
                description = "이메일 수신에 대한 동의 여부 (선택)",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        Boolean agreeEmail,

        @Schema(
                title = "서비스 이용약관 동의",
                example = "true",
                description = "서비스 이용약관에 대한 동의 여부",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "서비스 이용약관 동의는 필수 입니다.")
        @AssertTrue(message = "서비스 이용약관에 동의해야 합니다.")
        Boolean agreeTOS
    ){}
