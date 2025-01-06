package com.buysellgo.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    // tbl_order와 1:1
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    public enum DeliveryStatus {
        IN_DELIVERY, DELIVERED
    }

    public static Delivery of(Long orderId, DeliveryStatus deliveryStatus) {
        return Delivery.builder()
                .orderId(orderId)
                .createdAt(Timestamp.from(Instant.now()))
                .deliveryStatus(deliveryStatus)
                .build();
    }

    public Vo toVo(){
        return new Vo(deliveryId, orderId, createdAt, deliveryStatus);
    }

    private record Vo(Long deliveryId,
                      Long orderId,
                      Timestamp createdAt,
                      DeliveryStatus deliveryStatus) {
    }
}
