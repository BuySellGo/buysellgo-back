package com.buysellgo.promotionservice.dto;

import com.buysellgo.promotionservice.entity.Promotion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionRequestDto {

    @Schema(title = "Product ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product ID는 필수입니다.")
    private Long productId;

    @Schema(title = "Discount Rate", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "할인율은 필수입니다.")
    @Min(value = 1, message = "할인율은 1 이상이어야 합니다.")
    @Max(value = 100, message = "할인율은 100 이하이어야 합니다.")
    private Integer discountRate;

    @Schema(title = "프로모션 시작 시간", example="2025-01-15T01:55:34.756+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "프로모션 시작 시간은 필수 입니다.")
    private Timestamp startDate;

    @Schema(title = "프로모션 종료 시간", example="2025-01-18T01:55:34.756+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "프로모션 종료 시간은 필수 입니다.")
    private Timestamp endDate;

    @Schema(title = "승인여부", example="True", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "승인여부는 필수 입니다.")
    private Boolean isApproved;

    // startDate가 endDate보다 이전인지 확인하는 메서드
    @AssertTrue(message = "프로모션 시작 시간이 종료 시간보다 나중일 수 없습니다.")
    public boolean isStartDateBeforeEndDate() {
        if (startDate != null && endDate != null) {
            return !startDate.after(endDate);  // startDate가 endDate보다 이전이거나 같아야 유효
        }
        return true; // startDate와 endDate가 모두 null인 경우 검증하지 않음 (기본값으로 true)
    }

    public Promotion toEntity(Long sellerId) {
        return Promotion.of(sellerId, productId, discountRate, startDate, endDate, isApproved);
    }
}
