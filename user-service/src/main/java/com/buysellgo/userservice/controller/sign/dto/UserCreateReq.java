package com.buysellgo.userservice.controller.sign.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public record UserCreateReq(
        @Schema(title = "이메일", example = "test@test.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "이메일은 필수 입니다.")
        @Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
        @Pattern(
                regexp = "[^@]+@[^@]+\\.[^@]+",
                message = "올바른 이메일 형식이 아닙니다."
        )
        String email,

        @Schema(title = "비밀번호", example = "test1234!", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "비밀번호는 필수 입니다.")
        @Pattern(
                regexp = "^$|(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
                message = "비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."
        )
        String password,

        @Schema(title = "닉네임", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "닉네임은 필수 입니다.")
        @Size(max = 50, message = "닉네임은 50자를 초과할 수 없습니다.")
        String username,

        @Schema(title = "휴대폰 번호", example = "010-1234-5678", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "휴대폰 번호는 필수 입니다.")
        @Size(max = 30, message = "전화번호는 30자를 초과할 수 없습니다.")
        @Pattern(
                regexp = "^$|010-\\d{4}-\\d{4}$",
                message = "올바른 전화번호 형식이 아닙니다."
        )
        String phone,

        @Schema(title = "이메일 인증 여부", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "이메일 인증 여부는 필수 입니다.")
        @AssertTrue(message = "이메일 인증이 필요합니다.")
        Boolean emailCertified,

        @Schema(title = "개인정보 수집 및 이용 동의", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "개인정보 수집 및 이용 동의는 필수 입니다.")
        @AssertTrue(message = "개인정보 수집 및 이용에 동의해야 합니다.")
        Boolean agreePICU,

        @Schema(title = "이메일 수신 동의", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "이메일 수신 동의 여부는 필수 입니다.")
        Boolean agreeEmail,

        @Schema(title = "서비스 이용약관 동의", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "서비스 이용약관 동의는 필수 입니다.")
        @AssertTrue(message = "서비스 이용약관에 동의해야 합니다.")
        Boolean agreeTOS
) {}
