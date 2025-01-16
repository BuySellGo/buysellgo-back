package com.buysellgo.userservice.strategy.sign.dto;

import com.buysellgo.userservice.controller.sign.dto.AdminCreateReq;

public record AdminSignUpDto(
    String email,   
    String password
) implements SignUpDto {
    public static AdminSignUpDto from(AdminCreateReq req) {
        return new AdminSignUpDto(req.email(), req.password());
    }
}
