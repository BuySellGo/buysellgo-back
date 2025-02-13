package com.buysellgo.productservice.strategy.dto;

import com.buysellgo.productservice.entity.MainCategory;
import com.buysellgo.productservice.entity.SubCategory;
import com.buysellgo.productservice.entity.Season;
import com.buysellgo.productservice.controller.dto.ProductReq;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;  

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private String productName;
    private int price;
    private String companyName;
    private long sellerId;
    private String productImage;
    private String description;
    private int productStock;
    private int discountRate;
    private int deliveryFee;
    private MainCategory mainCategory;
    private SubCategory subCategory;
    private Season season;

    public static ProductDto from(ProductReq req, long sellerId) {
        return ProductDto.builder()
            .productName(req.productName())
            .price(req.price())
            .companyName(req.companyName())
            .sellerId(sellerId)
            .productImage(req.productImage())
            .description(req.description())
            .productStock(req.productStock())
            .discountRate(req.discountRate())
            .deliveryFee(req.deliveryFee())
            .mainCategory(req.mainCategory())
            .subCategory(req.subCategory())
            .season(req.season())
            .build();
}
}