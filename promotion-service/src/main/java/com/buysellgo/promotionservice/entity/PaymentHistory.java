package com.buysellgo.promotionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_payment_history")
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long userId;
    private Long productId;
    private Integer price;
    private Integer totalPrice;
    private Integer quantity;
    private PayMethod payMethod;
    private String status;
    private Timestamp paidAt;

    public enum PayMethod {
        KAKAOPAY, CARD
    }
}
