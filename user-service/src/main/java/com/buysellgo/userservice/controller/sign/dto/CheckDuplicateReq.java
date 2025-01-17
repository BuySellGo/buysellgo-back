package com.buysellgo.userservice.controller.sign.dto;

import com.buysellgo.userservice.common.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public record CheckDuplicateReq(
    @Schema(title = "이메일", example = "user@example.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Pattern(
        regexp = "^$|^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$",
        message = "올바른 이메일 형식이 아닙니다."
    )
    @Size(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
    String email,

    @Schema(title = "사용자명", example = "username123", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 50, message = "사용자명은 최대 50자까지 가능합니다.")
    String username,

    @Schema(title = "회사명", example = "myCompany", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 50, message = "회사명은 최대 50자까지 가능합니다.")
    String companyName,

    @Schema(title = "사용자 역할", example = "USER", description = "사용자 역할 (USER: 일반 사용자, SELLER: 판매자, ADMIN: 관리자)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "사용자 역할은 필수 입니다.")
    Role role
) {
    
}
