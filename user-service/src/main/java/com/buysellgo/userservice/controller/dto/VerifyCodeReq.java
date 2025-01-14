package com.buysellgo.userservice.controller.dto;

public record VerifyCodeReq(
        String email,
        SendType type,
        String code
) {}
