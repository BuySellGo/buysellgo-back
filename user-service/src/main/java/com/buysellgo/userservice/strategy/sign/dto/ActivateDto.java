package com.buysellgo.userservice.strategy.sign.dto;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.sign.dto.ActivateReq;

public record ActivateDto(
        String email,
        Role role
) {
    public static ActivateDto from(ActivateReq req) {
        return new ActivateDto(req.email(), req.role());
    }
}

