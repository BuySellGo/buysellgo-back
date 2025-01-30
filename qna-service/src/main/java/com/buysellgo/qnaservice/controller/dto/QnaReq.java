package com.buysellgo.qnaservice.controller.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public record QnaReq(
    @Schema(description = "상품 ID", example = "1")
    @NotNull(message = "상품 ID는 필수 입력 항목입니다.")
    long productId,
    @Schema(description = "판매자 ID", example = "1")
    @NotNull(message = "판매자 ID는 필수 입력 항목입니다.")
    long sellerId,
    @Schema(description = "비공개 여부", example = "true")
    @NotNull(message = "비공개 여부는 필수 입력 항목입니다.")
    boolean isPrivate,
    @Schema(description = "질문 내용", example = "질문 내용")
    @NotNull(message = "질문 내용은 필수 입력 항목입니다.")
    String content
) {

}
