package com.buysellgo.chatservice.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateChatReq(
    @Schema(description = "회원 ID", example = "1")
    @NotNull(message = "회원 ID는 필수 입력 항목입니다.")
    Long userId,

    @Schema(description = "관리자 ID", example = "1")
    @NotNull(message = "관리자 ID는 필수 입력 항목입니다.")
    Long adminId
) {}
