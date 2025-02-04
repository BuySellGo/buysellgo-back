package com.buysellgo.orderservice.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record CartCreateReq(
    @Schema(description = "상품 ID", example = "1")
    @NotNull(message = "상품 ID는 필수 입력 사항입니다.")
    long productId,
    @Schema(description = "상품 이름", example = "상품1")
    @NotNull(message = "상품 이름은 필수 입력 사항입니다.")
    String productName,
    @Schema(description = "판매자 ID", example = "1")
    @NotNull(message = "판매자 ID는 필수 입력 사항입니다.")
    long sellerId,
    @Schema(description = "회사 이름", example = "회사1")
    @NotNull(message = "회사 이름은 필수 입력 사항입니다.")
    String companyName,
    @Schema(description = "수량", example = "1")
    @NotNull(message = "수량은 필수 입력 사항입니다.")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    int quantity,
    @Schema(description = "가격", example = "10000")
    @NotNull(message = "가격은 필수 입력 사항입니다.")
    @Min(value = 1, message = "가격은 1 이상이어야 합니다.")
    int price
) {}
