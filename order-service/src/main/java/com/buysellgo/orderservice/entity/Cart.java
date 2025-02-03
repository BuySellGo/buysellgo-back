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

@Table(name = "tbl_cart")
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;

    @Column(name = "user_id", columnDefinition = "bigint", nullable = true, unique = false)
    private String userId;

    @Column(name = "product_id", columnDefinition = "bigint", nullable = true, unique = false)
    private String productId;

    @Column(name = "product_name", columnDefinition = "varchar(100)", nullable = true, unique = false)
    private String productName;

    @Column(name = "quantity", columnDefinition = "int", nullable = true, unique = false)
    private int quantity;

    @Column(name = "price", columnDefinition = "int", nullable = true, unique = false)
    private int price;


    public static Cart of(String userId, String productId, String productName, int quantity, int price){
        return Cart.builder()
                .userId(userId)
                .productId(productId)
                .productName(productName)
                .quantity(quantity)
                .price(price)
                .build();
    }

    public Vo toVo(){
        return new Vo(cartId, userId, productId, productName, quantity, price, version, createdAt, updatedAt);
    }
    
    public record Vo(
        long cartId,
        String userId,
        String productId,
        String productName,
        int quantity,
        int price,
        long version,
        Timestamp createdAt,
        Timestamp updatedAt
    ) {}
}


