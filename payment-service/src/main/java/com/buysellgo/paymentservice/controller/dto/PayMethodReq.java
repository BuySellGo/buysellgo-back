package com.buysellgo.paymentservice.controller.dto;

import com.buysellgo.paymentservice.entity.PayMethodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;



public record PayMethodReq(
    @Schema(description = "결제 수단 이름", example = "카카오페이")
    @NotBlank(message = "결제 수단 이름은 필수 입력 사항입니다.")
    String payMethodName,
    @Schema(description = "결제 수단 타입", example = "KAKAO_PAY")
    @NotNull(message = "결제 수단 타입은 필수 입력 사항입니다.")
    PayMethodType payMethodType,
    @Schema(description = "결제 수단 번호", example = "1234567890")
    @NotBlank(message = "결제 수단 번호는 필수 입력 사항입니다.")
    String payMethodNumber
) {}


