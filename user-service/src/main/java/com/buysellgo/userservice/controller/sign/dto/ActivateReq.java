package com.buysellgo.userservice.controller.sign.dto;

import com.buysellgo.userservice.common.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActivateReq (

    @Schema(title = "사용자 역할", example = "USER", description = "사용자 역할 (USER: 일반 사용자, SELLER: 판매자, ADMIN: 관리자)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "사용자 역할은 필수 입니다.")
    Role role,

    @Schema(title = "이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotNull(message = "이메일은 필수 입니다.")
    @Size(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
    String email
){}
