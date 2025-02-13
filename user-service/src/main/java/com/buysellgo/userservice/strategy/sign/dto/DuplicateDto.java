package com.buysellgo.userservice.strategy.sign.dto;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.sign.dto.CheckDuplicateReq;

public record DuplicateDto(
    String email,
    String username,
    String companyName,
    Role role
) {
    public static DuplicateDto from(CheckDuplicateReq req) {
        return new DuplicateDto(req.email(), req.username(), req.companyName(), req.role());
    }
}
