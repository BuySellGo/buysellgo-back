package com.buysellgo.productservice.entity;

import com.buysellgo.productservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId; // 상품 아이디

    @Column(name = "product_name",columnDefinition = "varchar(100)", nullable = false, unique = true)
    private String productName; // 상품명

    @Column(name = "price",columnDefinition = "int", nullable = false)
    private Integer price; // 가격

    @Column(name = "company_name",columnDefinition = "varchar(50)", nullable = false) 
    private String companyName; // 회사 이름

    @Column(name = "seller_id",columnDefinition = "bigint", nullable = false)
    private long sellerId; // 판매자 아이디

    @Column(name = "product_image",columnDefinition = "varchar(200)", nullable = false)
    private String productImage; // 상품 이미지 URL

    @Column(name = "description",columnDefinition = "varchar(500)", nullable = false)
    private String description; // 상품 설명

    @Column(name = "product_stock",columnDefinition = "int", nullable = false)
    private int productStock; // 재고 수량

    @Column(name = "discount_rate",columnDefinition = "int", nullable = false)
    private int discountRate; // 할인율 (0 ~ 100)

    @Column(name = "delivery_fee",columnDefinition = "int", nullable = false)
    private int deliveryFee; // 배송비

    @Enumerated(EnumType.STRING)
    @Column(name = "main_category", nullable = false)
    private MainCategory mainCategory; // 메인 카테고리

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_category", nullable = false)
    private SubCategory subCategory; // 서브 카테고리

    @Column(name = "season", nullable = false)
    @Enumerated(EnumType.STRING)
    private Season season; // 계절

    public static Product of(String productName, int price, String companyName, long sellerId, String productImage, String description, int productStock, int discountRate, int deliveryFee, MainCategory mainCategory, SubCategory subCategory, Season season) {
        return Product.builder()
                .productName(productName)
                .price(price)
                .companyName(companyName)
                .sellerId(sellerId)
                .productImage(productImage)
                .description(description)
                .productStock(productStock)
                .discountRate(discountRate)
                .deliveryFee(deliveryFee)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .season(season)
                .build();
    }

    public Vo toVo(){
        return new Vo(productId, productName, price, companyName, sellerId, productImage, description, productStock, discountRate, deliveryFee, mainCategory, subCategory, season, version, createdAt, updatedAt);
    }

    public record Vo(
        long productId,
        String productName,
        int price,
        String companyName,
        long sellerId,
        String productImage,
        String description,
        int productStock,
        int discountRate,
        int deliveryFee,
        MainCategory mainCategory,
        SubCategory subCategory,
        Season season,
        long version,
        Timestamp createdAt,
        Timestamp updatedAt
    ) {}
}
