package com.buysellgo.paymentservice.entity;

import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.*;
import com.buysellgo.paymentservice.common.entity.BaseEntity;  

@Entity
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_payment")
public class Payment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;
    @Column(name = "order_id", columnDefinition = "bigint", nullable = false, unique = false)
    private long orderId;
    @Column(name = "user_id", columnDefinition = "bigint", nullable = false, unique = false)
    private long userId;
    @Column(name = "product_id", columnDefinition = "bigint", nullable = false, unique = false)
    private long productId;
    @Column(name = "total_price", columnDefinition = "bigint", nullable = false, unique = false)
    private long totalPrice;
    @Column(name = "payment_method", columnDefinition = "varchar(255)", nullable = false, unique = false)
    private String paymentMethod;
    @Column(name = "group_id", columnDefinition = "bigint", nullable = false, unique = false)
    private long groupId;
    @Column(name = "payment_status", columnDefinition = "enum('PENDING', 'SUCCESS', 'FAIL')", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;    

    public static Payment of(long orderId, long userId, long productId, long totalPrice, String paymentMethod, long groupId){
        return Payment.builder()
            .orderId(orderId)
            .userId(userId)
            .productId(productId)
            .totalPrice(totalPrice)
            .paymentMethod(paymentMethod)
            .groupId(groupId)
            .paymentStatus(PaymentStatus.PENDING)
            .build();
    }

    public Vo toVo(){
        return new Vo(
            paymentId,
            orderId,
            userId,
            productId,
            totalPrice,
            paymentMethod,
            groupId,
            paymentStatus,
            version,
            createdAt,
            updatedAt
        );
    }


    public record Vo(
        long paymentId,
        long orderId,
        long userId,
        long productId,
        long totalPrice,
        String paymentMethod,
        long groupId,
        PaymentStatus paymentStatus,
        long version,
        Timestamp createdAt,
        Timestamp updatedAt
    ){}
}