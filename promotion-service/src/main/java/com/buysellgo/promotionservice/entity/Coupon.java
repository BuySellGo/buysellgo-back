package com.buysellgo.promotionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "coupon_title", columnDefinition = "varchar(100)")
    private String couponTitle;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "eligible_recipient")
    private EligibleRecipient eligibleRecipient;

    // 쿠폰 발급 내역 (OneToMany 관계 설정)
    @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CouponIssuance> couponIssuanceList;

    public enum EligibleRecipient {
        NORMAL, VIP
    }

    public static Coupon of(String couponTitle, Integer discountRate, EligibleRecipient eligibleRecipient) {
        return Coupon.builder()
                .couponTitle(couponTitle)
                .createdAt(Timestamp.from(Instant.now()))
                .discountRate(discountRate)
                .eligibleRecipient(eligibleRecipient)
                .build();
    }

    public Vo toVo(){
        return new Vo(id, createdAt, couponTitle, discountRate, eligibleRecipient);
    }


    public record Vo(Long id,
                      Timestamp createdAt,
                      String couponTitle,
                      Integer discountRate,
                      EligibleRecipient eligibleRecipient) {
    }

}
