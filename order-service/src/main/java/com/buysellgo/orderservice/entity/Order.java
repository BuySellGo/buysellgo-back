package com.buysellgo.orderservice.entity;

import java.sql.Timestamp;

import com.buysellgo.orderservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
@Builder
@Table(name = "tbl_order")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "product_id", columnDefinition = "bigint", nullable = true, unique = false)
    private String productId;

    @Column(name = "product_name", columnDefinition = "varchar(100)", nullable = true, unique = false)
    private String productName;

    @Column(name = "seller_id", columnDefinition = "bigint", nullable = true, unique = false)
    private String sellerId;

    @Column(name = "company_name", columnDefinition = "varchar(100)", nullable = true, unique = false)
    private String companyName;

    @Column(name = "user_id", columnDefinition = "bigint", nullable = true, unique = false)
    private String userId;

    @Column(name = "quantity", columnDefinition = "int", nullable = true, unique = false)
    private int quantity;

    @Column(name = "total_price", columnDefinition = "int", nullable = true, unique = false)
    private int totalPrice;

    @Column(name = "memo", columnDefinition = "varchar(200)", nullable = true, unique = false)
    private String memo;

    @Column(name = "payment_method", columnDefinition = "enum('CREDIT_CARD', 'KAKAO_PAY')", nullable = true, unique = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "status", columnDefinition = "enum('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED')", nullable = true, unique = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;



    public static Order of(String productId, String productName, String sellerId, String companyName, String userId, int quantity, int totalPrice, String memo, PaymentMethod paymentMethod) {
        return Order.builder()
                .productId(productId)
                .productName(productName)
                .sellerId(sellerId)
                .companyName(companyName)
                .userId(userId)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .memo(memo)
                .paymentMethod(paymentMethod)
                .build();
    }

    public Vo toVo(){
        return new Vo(orderId, productId, productName, sellerId, companyName, userId, quantity, totalPrice, memo, paymentMethod, status, version, createdAt, updatedAt);
    }


    public record Vo(
        long orderId,
        String productId,
        String productName,
        String sellerId,
        String companyName,
        String userId,
        int quantity,
        int totalPrice,
        String memo,
        PaymentMethod paymentMethod,
        OrderStatus status,
        long version,
        Timestamp createdAt,
        Timestamp updatedAt

    ){}
}
