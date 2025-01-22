package com.buysellgo.helpdeskservice.dto;

import com.buysellgo.helpdeskservice.entity.Notice;
import com.buysellgo.helpdeskservice.entity.OneToOneInquiry;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryRequestDto {

    @Schema(title = "1:1 문의 내용", example="내일 뭐 사지?", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "1:1 문의 내용은 필수 입니다.")
    @Pattern(
            regexp = "^.{10,1000}$",
            message = "문의 내용은 최소 10자 이상이며, 최대 1000자 이내여야 합니다."
    )
    private String content;

    public OneToOneInquiry toEntity(Long userId){
        return OneToOneInquiry.of(userId, content);
    }

}
