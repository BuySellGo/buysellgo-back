package com.buysellgo.chatservice.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
public record LeaveChatReq(
    @Schema(description = "채팅방 ID", example = "1")
    @NotNull(message = "채팅방 ID는 필수 입력 항목입니다.")
    Long chattingId 
) {}

