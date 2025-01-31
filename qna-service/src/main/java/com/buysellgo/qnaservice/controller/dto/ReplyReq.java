package com.buysellgo.qnaservice.controller.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReplyReq( 
    @Schema(description = "질문 ID", example = "1")
    @NotNull(message = "질문 ID는 필수 입력 항목입니다.")
    long qnaId,
    @Schema(description = "답변 내용", example = "답변 내용")
    @NotNull(message = "답변 내용은 필수 입력 항목입니다.")
    String content
) {

}
