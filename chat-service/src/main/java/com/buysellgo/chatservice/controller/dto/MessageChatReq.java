package com.buysellgo.chatservice.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;  
import jakarta.validation.constraints.NotNull;

public record MessageChatReq(
    @Schema(description = "채팅방 ID", example = "1")
    @NotNull(message = "채팅방 ID는 필수 입력 항목입니다.")
    Long chattingId,

    @Schema(description = "메시지 내용", example = "안녕하세요")
    @NotNull(message = "메시지 내용은 필수 입력 항목입니다.")
    String content,

    @Schema(description = "메시지 보낸 사람 ID", example = "1")
    @NotNull(message = "메시지 보낸 사람 ID는 필수 입력 항목입니다.")
    Long senderId
) {}

