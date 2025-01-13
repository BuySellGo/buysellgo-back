package com.buysellgo.promotionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_coupon_issuance")
public class CouponIssuance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="profile_id")
    private Long profileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "created_at")
    private Timestamp createdAt;

    // 생성 메서드
    public static CouponIssuance of(Long profileId, Coupon coupon) {
        return CouponIssuance.builder()
                .profileId(profileId)
                .coupon(coupon)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
    }

    // VO 객체로 변환하는 메서드
    public Vo toVo() {
        return new Vo(id, profileId, coupon.getId(), createdAt);
    }

    // DTO 객체로 사용할 VO (Value Object)
    private record Vo(Long id,
                      Long profileId,
                      Long couponId,
                      Timestamp createdAt) {
    }

}
