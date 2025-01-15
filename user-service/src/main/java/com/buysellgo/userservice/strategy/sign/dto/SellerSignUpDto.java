package com.buysellgo.userservice.strategy.sign.dto;

import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.controller.sign.dto.SellerCreateReq;

public record SellerSignUpDto(
    String email,
    String password,
    String presidentName,
    String companyName,
    Address address,
    String businessRegistrationNumber,
    String businessRegistrationNumberImg
) implements SignUpDto {
    public static SellerSignUpDto from(SellerCreateReq req) {
        return new SellerSignUpDto(
            req.email(),
            req.password(),
            req.presidentName(),
            req.companyName(),
            req.address(),
            req.businessRegistrationNumber(),
            req.businessRegistrationNumberImg()
        );
    }
}