package com.buysellgo.userservice.controller.sign.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminCreateReq(
    @Schema(title = "이메일", example = "admin@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotNull(message = "이메일은 필수 입니다.")
    @Size(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
    @Pattern(
        regexp = "^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$",
        message = "올바른 이메일 형식이 아닙니다."
    )
    String email,

    @Schema(title = "비밀번호", example = "securePassword123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "비밀번호는 필수 입니다.")
    @Size(min = 8, max = 200, message = "비밀번호는 최소 8자 이상, 최대 200자까지 가능합니다.")
    @Pattern(
        regexp = "^$|(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
        message = "비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."
    )
    String password
) {}

