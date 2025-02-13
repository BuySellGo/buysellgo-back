package com.buysellgo.paymentservice.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.buysellgo.paymentservice.controller.dto.PaymentReq;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private long orderId;
    private long userId;
    private long productId;
    private long totalPrice;
    private String paymentMethod;
    private long groupId;

    public static PaymentDto from(PaymentReq req, long userId){
        return PaymentDto.builder()
            .orderId(req.orderId())
            .userId(userId)
            .productId(req.productId())
            .totalPrice(req.totalPrice())
            .paymentMethod(req.paymentMethod())
            .groupId(req.groupId())
            .build();

    }
}