package com.buysellgo.statisticsservice.dto;

import com.buysellgo.statisticsservice.entity.AccessStatistics;
import com.buysellgo.statisticsservice.entity.SalesStatistics;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccessStatRequestDto {
    @Schema(title = "User ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "User ID는 필수입니다.")
    private Long userId;

    @Schema(title = "접속 일시", example="2025-01-15T01:55:34.756+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "접속 일시는 필수 입니다.")
    private Timestamp accessDateTime;

    @Schema(title = "접속 IP Address", example = "10.1.3.123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "접속 IP Address는 필수입니다.")
    private String accessIp;

    public AccessStatistics toEntity() {
        return AccessStatistics.of(userId, accessIp, accessDateTime);
    }

}
