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

    @Column(name = "seller_id", columnDefinition = "bigint", nullable = true, unique = false)
    private String sellerId;

    @Column(name = "company_name", columnDefinition = "varchar(100)", nullable = true, unique = false)
    private String companyName;

    @Column(name = "quantity", columnDefinition = "int", nullable = true, unique = false)
    private int quantity;

    @Column(name = "price", columnDefinition = "int", nullable = true, unique = false)
    private int price;

    @Column(name = "group_id", columnDefinition = "bigint", nullable = false, unique = false)
    private long groupId;




    public static Cart of(String userId, String productId, String productName, String sellerId, String companyName, int quantity, int price, long groupId){
        return Cart.builder()
                .userId(userId)
                .productId(productId)
                .productName(productName)
                .sellerId(sellerId)
                .companyName(companyName)
                .quantity(quantity)
                .price(price)
                .groupId(groupId)
                .build();

    }

    public Vo toVo(){
        return new Vo(cartId, userId, productId, productName, sellerId, companyName, quantity, price, groupId, version, createdAt, updatedAt);
    }
    
    public record Vo(
        long cartId,
        String userId,
        String productId,
        String productName,
        String sellerId,
        String companyName,
        int quantity,
        int price,
        long groupId,
        long version,
        Timestamp createdAt,
        Timestamp updatedAt


    ) {}
}


