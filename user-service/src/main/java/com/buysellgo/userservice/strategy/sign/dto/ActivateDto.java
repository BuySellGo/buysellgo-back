package com.buysellgo.userservice.strategy.sign.dto;

import com.buysellgo.userservice.common.entity.Authorization;

public record ActivateDto(

        Authorization isApproved,
        Authorization status
) {}
