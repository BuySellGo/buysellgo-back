package com.buysellgo.userservice.strategy.sign.dto;

import com.buysellgo.userservice.controller.sign.dto.UserCreateReq;

public record UserSignUpDto(
    String email,
    String password,
    String username,
    String phone,
    Boolean emailCertified,
    Boolean agreePICU,
    Boolean agreeEmail,
    Boolean agreeTOS
) implements SignUpDto {
    public static UserSignUpDto from(UserCreateReq req) {
        return new UserSignUpDto(
            req.email(),
            req.password(),
            req.username(),
            req.phone(),
            req.emailCertified(),
            req.agreePICU(),
            req.agreeEmail(),
            req.agreeTOS()
        );
    }
}