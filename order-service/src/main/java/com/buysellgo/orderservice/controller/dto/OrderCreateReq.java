package com.buysellgo.orderservice.controller.dto;

import com.buysellgo.orderservice.entity.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
public record OrderCreateReq(
    @Schema(description = "상품 ID", example = "1")
    @NotNull(message = "상품 ID는 필수 입력 항목입니다.")
    long productId,
    @Schema(description = "상품 이름", example = "상품1")
    @NotNull(message = "상품 이름은 필수 입력 항목입니다.")
    String productName,
    @Schema(description = "판매자 ID", example = "1")
    @NotNull(message = "판매자 ID는 필수 입력 항목입니다.")
    long sellerId,
    @Schema(description = "판매자 회사명", example = "회사1")
    @NotNull(message = "판매자 회사명은 필수 입력 항목입니다.")
    String companyName,
    @Schema(description = "수량", example = "1")
    @NotNull(message = "수량은 필수 입력 항목입니다.")
    int quantity,
    @Schema(description = "총 가격", example = "10000")
    @NotNull(message = "총 가격은 필수 입력 항목입니다.")
    int totalPrice,
    @Schema(description = "메모", example = "메모1")
    @NotNull(message = "메모는 필수 입력 항목입니다.")
    String memo,
    @Schema(description = "결제 방법", example = "KAKAO_PAY")
    @NotNull(message = "결제 방법은 필수 입력 항목입니다.")
    PaymentMethod paymentMethod
) {
} 
