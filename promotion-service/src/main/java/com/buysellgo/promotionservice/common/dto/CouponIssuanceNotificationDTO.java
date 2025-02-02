package com.buysellgo.promotionservice.common.dto;

import com.buysellgo.promotionservice.entity.CouponIssuance;
import com.buysellgo.promotionservice.entity.CouponNotification;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponIssuanceNotificationDTO {
    private Long userId;
    private CouponIssuance couponIssuance;
    private CouponNotification couponNotification;
}
