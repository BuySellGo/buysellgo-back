package com.buysellgo.promotionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
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
    @Column(name="id")
    private Long id;

    @Column(name="seller_id")
    private Long sellerId;

    @Column(name="product_id")
    private Long productId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id", nullable = false)
    private Banners banners;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name= "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_approved")
    private Boolean isApproved;

    public static Promotion of(Long sellerId,
                               Long productId,
                               Banners banners,
                               Integer discountRate,
                               LocalDateTime startDate,
                               LocalDateTime endDate,
                               Boolean isApproved){

        return Promotion.builder()
                .banners(banners)
                .createdAt(LocalDateTime.now(ZoneId.of("UTC")))
                .discountRate(discountRate)
                .startDate(startDate)
                .endDate(endDate)
                .isApproved(isApproved)
                .build();
    }

    public Vo toVo() {
        return new Vo(id, sellerId, productId, banners.getId(), createdAt, discountRate, startDate, endDate, isApproved);
    }

    private record Vo(Long id,
                      Long sellerId,
                      Long productId,
                      Long bannerId,
                      LocalDateTime createdAt,
                      Integer discountRate,
                      LocalDateTime startDate,
                      LocalDateTime endDate,
                      Boolean isApproved) {
    }
}
