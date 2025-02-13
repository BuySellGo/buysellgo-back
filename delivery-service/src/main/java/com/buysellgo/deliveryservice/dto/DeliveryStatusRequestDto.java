package com.buysellgo.deliveryservice.dto;

import com.buysellgo.deliveryservice.entity.Delivery;
import com.buysellgo.deliveryservice.entity.Delivery.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryStatusRequestDto {

    @Schema(description = "주문 ID", example = "12345")
    @NotNull(message = "주문 ID는 필수입니다.")
    @Positive(message = "주문 ID는 양수여야 합니다.")
    private Long orderId;

    @Schema(description = "배송 생성 날짜", example = "2025-01-24T12:34:56")
    @NotNull(message = "생성 날짜는 필수입니다.")
    private Timestamp createdAt;

    @Schema(description = "배송 상태", example = "IN_DELIVERY", allowableValues = {"IN_DELIVERY", "DELIVERED"})
    @NotNull(message = "배송 상태는 필수입니다.")
    private DeliveryStatus deliveryStatus;

    public Delivery toEntity() {
        return Delivery.of(orderId, createdAt, deliveryStatus);
    }

}
