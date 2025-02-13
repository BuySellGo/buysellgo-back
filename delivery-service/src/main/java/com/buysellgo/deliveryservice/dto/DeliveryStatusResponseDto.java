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
public class DeliveryStatusResponseDto {

    private Long id;
    private Long orderId;
    private Timestamp createdAt;
    private DeliveryStatus deliveryStatus;

}
