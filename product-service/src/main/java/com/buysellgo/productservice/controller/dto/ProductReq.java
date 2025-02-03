package com.buysellgo.productservice.controller.dto;

import com.buysellgo.productservice.entity.MainCategory;
import com.buysellgo.productservice.entity.SubCategory;
import com.buysellgo.productservice.entity.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ProductReq(
    @Schema(description = "상품 이름", example = "상품 이름")
    @NotNull(message = "상품 이름은 필수 입력 항목입니다.")
    String productName,
   
    @Schema(description = "상품 가격", example = "10000")
    @NotNull(message = "상품 가격은 필수 입력 항목입니다.")
    int price,
   
    @Schema(description = "상품 회사 이름", example = "상품 회사 이름")
    @NotNull(message = "상품 회사 이름은 필수 입력 항목입니다.")
    String companyName,

    @Schema(description = "상품 이미지", example = "상품 이미지")
    @NotNull(message = "상품 이미지는 필수 입력 항목입니다.")
    String productImage,

    @Schema(description = "상품 설명", example = "상품 설명")
    @NotNull(message = "상품 설명은 필수 입력 항목입니다.")
    String description,

    @Schema(description = "상품 재고", example = "100")
    @NotNull(message = "상품 재고는 필수 입력 항목입니다.")
    int productStock,
    
    @Schema(description = "상품 할인 비율", example = "10")
    @NotNull(message = "상품 할인 비율은 필수 입력 항목입니다.")
    int discountRate,
    
    @Schema(description = "상품 배송비", example = "3000")
    @NotNull(message = "상품 배송비는 필수 입력 항목입니다.")
    int deliveryFee,
    
    @Schema(description = "상품 대분류", example = "FASHION")
    @NotNull(message = "상품 대분류는 필수 입력 항목입니다.")
    MainCategory mainCategory,
    
    @Schema(description = "상품 소분류", example = "WOMENS_CLOTHI")
    @NotNull(message = "상품 소분류는 필수 입력 항목입니다.")
    SubCategory subCategory,
    
    @Schema(description = "상품 계절", example = "SPRING")
    @NotNull(message = "상품 계절은 필수 입력 항목입니다.")
    Season season
) {
}
