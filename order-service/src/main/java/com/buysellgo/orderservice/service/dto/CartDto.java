package com.buysellgo.orderservice.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.buysellgo.orderservice.controller.dto.CartCreateReq;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    long userId;
    long productId;
    String productName;
    long sellerId;
    String companyName;
    int quantity;
    int price;


    public static CartDto from(CartCreateReq req, long userId){
        return CartDto.builder()
            .userId(userId)
            .productId(req.productId())
            .productName(req.productName())
            .sellerId(req.sellerId())
            .companyName(req.companyName())
            .quantity(req.quantity())
            .price(req.price())
            .build();
    }
}
