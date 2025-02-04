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
    private long userId;


    @Column(name = "product_id", columnDefinition = "bigint", nullable = true, unique = false)
    private long productId;


    @Column(name = "product_name", columnDefinition = "varchar(100)", nullable = true, unique = false)
    private String productName;

    @Column(name = "seller_id", columnDefinition = "bigint", nullable = true, unique = false)
    private long sellerId;

    @Column(name = "company_name", columnDefinition = "varchar(100)", nullable = true, unique = false)
    private String companyName;


    @Column(name = "quantity", columnDefinition = "int", nullable = true, unique = false)
    private int quantity;

    @Column(name = "price", columnDefinition = "int", nullable = true, unique = false)
    private int price;




    public static Cart of(long userId, long productId, String productName, long sellerId, String companyName, int quantity, int price){
        return Cart.builder()
                .userId(userId)
                .productId(productId)
                .productName(productName)
                .sellerId(sellerId)
                .companyName(companyName)
                .quantity(quantity)
                .price(price)
                .build();


    }

    public Vo toVo(){
        return new Vo(cartId, userId, productId, productName, sellerId, companyName, quantity, price, version, createdAt, updatedAt);
    }
    

    public record Vo(
        long cartId,
        long userId,
        long productId,
        String productName,
        long sellerId,
        String companyName,
        int quantity,
        int price,

        long version,
        Timestamp createdAt,
        Timestamp updatedAt



    ) {}
}


