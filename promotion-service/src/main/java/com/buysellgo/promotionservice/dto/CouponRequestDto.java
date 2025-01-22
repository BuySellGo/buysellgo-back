package com.buysellgo.promotionservice.dto;

import com.buysellgo.promotionservice.entity.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;

import static com.buysellgo.promotionservice.entity.Coupon.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponRequestDto {

    @Schema(title = "쿠폰 제목", example = "50% 할인 쿠폰", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "쿠폰 제목은 필수 입니다.")
    @Pattern(
            regexp = "^.{2,100}$",
            message = "쿠폰 제목은 최소 2자 이상이며, 최대 100자 이내여야 합니다."
    )
    private String couponTitle;

    @Schema(title = "Discount Rate", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "할인율은 필수입니다.")
    @Min(value = 1, message = "할인율은 1 이상이어야 합니다.")
    @Max(value = 100, message = "할인율은 100 이하이어야 합니다.")
    private Integer discountRate;

    @Schema(description = "발급대상", example = "NORMAL", allowableValues = {"NORMAL", "VIP"})
    @NotNull(message = "발급대상은 필수 입니다.")
    private EligibleRecipient eligibleRecipient;

    public Coupon toEntity() {
        return Coupon.of(couponTitle, discountRate, eligibleRecipient);
    }


}
