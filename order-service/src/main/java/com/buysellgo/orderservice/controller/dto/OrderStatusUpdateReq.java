package com.buysellgo.orderservice.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import com.buysellgo.orderservice.entity.OrderStatus;

public record OrderStatusUpdateReq(
    @Schema(description = "주문 ID", example = "1")
    @NotNull(message = "주문 ID는 필수 입력 항목입니다.")
    long orderId,
    @Schema(description = "주문 상태", example = "CANCELLED")
    @NotNull(message = "주문 상태는 필수 입력 항목입니다.")
    OrderStatus orderStatus

) {
}
