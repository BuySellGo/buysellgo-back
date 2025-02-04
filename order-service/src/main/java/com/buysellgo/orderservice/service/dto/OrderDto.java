package com.buysellgo.orderservice.service.dto;

import com.buysellgo.orderservice.entity.PaymentMethod;
import com.buysellgo.orderservice.controller.dto.OrderCreateReq;
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

public class OrderDto {
    long productId;
    String productName;
    long sellerId;
    String companyName;
    long userId;
    int quantity;
    int totalPrice;
    String memo;
    PaymentMethod paymentMethod;

    public static OrderDto from(OrderCreateReq req, long userId){
        return OrderDto.builder()
            .productId(req.productId())
            .productName(req.productName())
            .sellerId(req.sellerId())
            .companyName(req.companyName())
            .userId(userId)
            .quantity(req.quantity())
            .totalPrice(req.totalPrice())
            .memo(req.memo())
            .paymentMethod(req.paymentMethod())
            .build();
    }   
}
