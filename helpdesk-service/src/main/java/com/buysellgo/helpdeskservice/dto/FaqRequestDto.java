package com.buysellgo.helpdeskservice.dto;

import com.buysellgo.helpdeskservice.entity.Faq;
import com.buysellgo.helpdeskservice.entity.FaqGroup;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "FAQ 생성 및 업데이트를 위한 DTO")
public class FaqRequestDto {
    @Schema(description = "FAQ 그룹 정보", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "FAQ 그룹 정보는 필수 입니다.")
    private FaqGroup faqGroup;

    @Schema(title = "FAQ 생성 시간", example="2025-01-18T01:55:34.756+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "시작 시간은 필수 입니다.")
    private Timestamp createdAt;

    @Schema(title = "FAQ 제목", example="배송지 주소 변경 방법", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "FAQ 제목은 필수 입니다.")
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9\\s!?,.=-_]{1,100}$",
            message = "제목은 100자 이하이며, 특수문자는 마지막에 !, ?, - 만 사용할 수 있습니다."
    )
    private String faqTitle;

    @Schema(title = "FAQ 내용", example="배송지 주소는 myPage에서 변경하세요.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "FAQ 내용은 필수 입니다.")
    @Pattern(
            regexp = "^.{10,1000}$",
            message = "FAQ 내용은 최소 10자 이상이며, 최대 1000자 이내여야 합니다."
    )
    private String faqContent;

    public Faq toEntity() {
        return Faq.of(faqGroup, faqTitle, faqContent);
    }
}
