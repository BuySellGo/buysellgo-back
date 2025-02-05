package com.buysellgo.paymentservice.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record PaymentReq(
    @Schema(description = "주문 아이디", example = "1")
    @NotNull(message = "주문 아이디는 필수 입력 사항입니다.")
    long orderId,
    @Schema(description = "상품 아이디", example = "1")
    @NotNull(message = "상품 아이디는 필수 입력 사항입니다.")
    long productId,
    @Schema(description = "수량", example = "1")
    @NotNull(message = "수량은 필수 입력 사항입니다.")
    int quantity,
    @Schema(description = "총 가격", example = "10000")
    @NotNull(message = "총 가격은 필수 입력 사항입니다.")
    long totalPrice,
    @Schema(description = "결제 방법", example = "카카오페이")
    @NotBlank(message = "결제 방법은 필수 입력 사항입니다.")
    String paymentMethod,
    @Schema(description = "그룹 아이디", example = "1")
    @NotNull(message = "그룹 아이디는 필수 입력 사항입니다.")
    long groupId


) {
}
