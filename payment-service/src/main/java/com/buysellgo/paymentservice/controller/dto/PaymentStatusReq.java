package com.buysellgo.paymentservice.controller.dto;

import com.buysellgo.paymentservice.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PaymentStatusReq(
    @Schema(description = "결제 아이디", example = "1")

    @NotNull(message = "결제 아이디는 필수 입력 사항입니다.")
    long paymentId,
    @Schema(description = "결제 상태", example = "FAIL")
    @NotNull(message = "결제 상태는 필수 입력 사항입니다.")
    PaymentStatus status
) {

}
