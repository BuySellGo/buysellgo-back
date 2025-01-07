package com.buysellgo.userservice.service.dto;

public sealed interface SignUpDto permits UserSignUpDto, SellerSignUpDto {
    // 공통 메서드 없이 타입 안전성만 확보
}
