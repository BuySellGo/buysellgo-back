package com.buysellgo.statisticsservice.dto;

import com.buysellgo.statisticsservice.entity.SalesStatistics;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "매출 통계 기록을 위한 DTO")
public class SalesStatRequestDto {

    @Schema(title = "Seller ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Seller ID는 필수입니다.")
    private Long sellerId;

    @Schema(title = "매출 발생 일시", example="2025-01-15T01:55:34.756+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "매출 발생 시간은 필수 입니다.")
    private Timestamp createdAt;

    @Schema(title = "매출", example = "10000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "매출은 필수입니다.")
    @Positive(message = "매출은 양수 값이어야 합니다.")
    private Long salesAmount;

    public SalesStatistics toEntity() {
        return SalesStatistics.of(sellerId, createdAt, salesAmount);
    }

}
