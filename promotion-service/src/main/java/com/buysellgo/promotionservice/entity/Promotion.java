package com.buysellgo.promotionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "seller_id")
    private Long sellerId;  // userId와 같음

    @Column(name = "product_id")
    private Long productId;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "banner_id", nullable = false)
//    private Banners banners;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @OneToOne(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Banners banners;

    public static Promotion of(Long sellerId,
                               Long productId,
//                               Banners banners,
                               Integer discountRate,
                               Timestamp startDate,
                               Timestamp endDate,
                               Boolean isApproved) {

        return Promotion.builder()
//                .banners(banners)
                .sellerId(sellerId)
                .productId(productId)
                .createdAt(Timestamp.from(Instant.now()))
                .discountRate(discountRate)
                .startDate(startDate)
                .endDate(endDate)
                .isApproved(isApproved)
                .build();
    }

    public Vo toVo() {
//        return new Vo(id, sellerId, productId, banners.getId(), createdAt, discountRate, startDate, endDate, isApproved);
        return new Vo(id, sellerId, productId, createdAt, discountRate, startDate, endDate, isApproved);
    }

    public record Vo(Long id,
                     Long sellerId,
                     Long productId,
                     Timestamp createdAt,
                     Integer discountRate,
                     Timestamp startDate,
                     Timestamp endDate,
                     Boolean isApproved) {
    }
}
