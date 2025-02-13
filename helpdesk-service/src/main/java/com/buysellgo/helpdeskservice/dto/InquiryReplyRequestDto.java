package com.buysellgo.helpdeskservice.dto;

import com.buysellgo.helpdeskservice.entity.OneToOneInquiry;
import com.buysellgo.helpdeskservice.entity.OneToOneInquiryReply;
import com.buysellgo.helpdeskservice.repository.OneToOneInquiryRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryReplyRequestDto {

    @Schema(title = "Inquiry ID", example="1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Inquiry ID는 필수입니다.")
    private Long oneToOneInquiryId;

    @Schema(title = "1:1 문의 답변 내용", example="100인치 TV 구매를 추천합니다.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "1:1 문의 답변 내용은 필수 입니다.")
    @Pattern(
            regexp = "^.{10,1000}$",
            message = "문의 답변 내용은 최소 10자 이상이며, 최대 1000자 이내여야 합니다."
    )
    private String content;

    public OneToOneInquiryReply toEntity(Long userId, OneToOneInquiryRepository oneToOneInquiryRepository) {

        // Inquiry 구성
        OneToOneInquiry oneToOneInquiry = oneToOneInquiryRepository.findById(oneToOneInquiryId).orElseThrow(
                () -> new EntityNotFoundException("Inquiry Id: " + oneToOneInquiryId + "} not found")
        );

        return OneToOneInquiryReply.of(oneToOneInquiry, userId, content);
    }

}
