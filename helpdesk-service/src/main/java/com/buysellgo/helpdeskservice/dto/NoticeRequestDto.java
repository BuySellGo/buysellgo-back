package com.buysellgo.helpdeskservice.dto;

import com.buysellgo.helpdeskservice.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeRequestDto {
//    @Schema(title = "User ID", example="1", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotEmpty(message = "User ID는 필수입니다.")
//    @Pattern(
//            regexp = "^\\d{1,19}$",
//            message = "데이터 타입이 잘못 되었습니다."
//    )
//    private Long userId;

    @Schema(title = "공지사항 제목", example="시스템 일시 중단", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "제목은 필수 입니다.")
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9\\s!?,.=-_]{1,100}$",
            message = "제목은 100자 이하이며, 특수문자는 마지막에 !, ?, - 만 사용할 수 있습니다."
    )
    private String title;

    @Schema(title = "공지 시작 시간", example="2025-01-15T01:55:34.756+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "시작 시간은 필수 입니다.")
//    @Pattern(
//            regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{3})?([+-]\\d{2}:\\d{2}|Z)$\n",
//            message = "2025-01-15T01:55:34.756+00:00 형식을 사용하세요."
//    )
    private Timestamp startDate;

    @Schema(title = "공지 종료 시간", example="2025-01-18T01:55:34.756+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "시작 시간은 필수 입니다.")
//    @Pattern(
//            regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{3})?([+-]\\d{2}:\\d{2}|Z)$\n",
//            message = "2025-01-18T01:55:34.756+00:00 형식을 사용하세요."
//    )
    private Timestamp endDate;

    @Schema(title = "공지사항 내용", example="23:00~24:00 시스템 일시 중단", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "공지사항 내용은 필수 입니다.")
    @Pattern(
            regexp = "^.{10,1000}$",
            message = "공지사항은 최소 10자 이상이며, 최대 1000자 이내여야 합니다."
    )
    private String content;

    public Notice toEntity(long userId){
        return Notice.of(userId, title, startDate, endDate, content);
    }
}
