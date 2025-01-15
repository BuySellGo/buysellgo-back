package com.buysellgo.userservice.controller.info.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import com.buysellgo.userservice.common.entity.Role;

public record InfoListReq(
    @Schema(description = "회원 역할", example = "USER" , requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "role은 필수 입력 값입니다." )   
    Role role
) {}
