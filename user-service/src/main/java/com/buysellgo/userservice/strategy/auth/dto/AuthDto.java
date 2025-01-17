package com.buysellgo.userservice.strategy.auth.dto;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.auth.dto.JwtCreateReq;
import com.buysellgo.userservice.controller.auth.dto.KeepLogin;


public record AuthDto(
    String email,
    String password,
    Role role,
    KeepLogin keepLogin
) {
    public static AuthDto from(JwtCreateReq req) {
        return new AuthDto(
            req.email(),
            req.password(),
            req.role(),
            req.keepLogin()
        );
    }
}
