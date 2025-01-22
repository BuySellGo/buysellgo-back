package com.buysellgo.promotionservice.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssuedCouponResponseDto {

    private Long couponIssuanceId;
    private Long userId;
    private Long couponId;
    private String couponTitle;
    private Integer discountRate;
    private Timestamp createdAt; // 쿠폰발급일

}
