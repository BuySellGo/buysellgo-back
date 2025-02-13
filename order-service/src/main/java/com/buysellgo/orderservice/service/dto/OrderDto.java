package com.buysellgo.orderservice.service.dto;

import com.buysellgo.orderservice.entity.PaymentMethod;
import com.buysellgo.orderservice.controller.dto.OrderCreateReq;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.buysellgo.orderservice.entity.Cart;
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

    public static OrderDto from(Cart cart, String memo, PaymentMethod paymentMethod){
        return OrderDto.builder()
            .productId(cart.getProductId())
            .productName(cart.getProductName())
            .sellerId(cart.getSellerId())
            .companyName(cart.getCompanyName())
            .userId(cart.getUserId())
            .quantity(cart.getQuantity())
            .totalPrice(cart.getPrice()*cart.getQuantity())
            .memo(memo)
            .paymentMethod(paymentMethod)
            .build();
    }
    
}
