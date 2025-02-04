package com.buysellgo.paymentservice.service.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.buysellgo.paymentservice.controller.dto.PayMethodReq;
import com.buysellgo.paymentservice.entity.PayMethodType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayMethodDto {
    private long userId;
    private String payMethodName;
    private PayMethodType payMethodType;
    private String payMethodNumber;

    public static PayMethodDto from(PayMethodReq req, long userId){
        return PayMethodDto.builder()
            .userId(userId)
            .payMethodName(req.payMethodName())
            .payMethodType(req.payMethodType())
            .payMethodNumber(req.payMethodNumber())
            .build();   
    }
}
