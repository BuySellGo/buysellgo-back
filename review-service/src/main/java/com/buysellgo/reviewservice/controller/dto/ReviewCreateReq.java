package com.buysellgo.reviewservice.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewCreateReq(
    @Schema(description = "상품 ID")
    @NotNull(message = "상품 ID는 필수 입력 항목입니다.")
    long productId,
    @Schema(description = "판매자 ID")
    @NotNull(message = "판매자 ID는 필수 입력 항목입니다.")
    long sellerId,
    @Schema(description = "주문 ID")
    @NotNull(message = "주문 ID는 필수 입력 항목입니다.")
    long orderId,
    @Schema(description = "별점")
    @NotNull(message = "별점은 필수 입력 항목입니다.")
    @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하이어야 합니다.")
    int starRating,
    @Schema(description = "리뷰 내용")
    @NotNull(message = "리뷰 내용은 필수 입력 항목입니다.")
    @Size(max = 1000, message = "리뷰 내용은 최대 1000자까지 허용됩니다.")
    String content,
    @Schema(description = "리뷰 이미지 URL")
    String imageUrl
) {
}
