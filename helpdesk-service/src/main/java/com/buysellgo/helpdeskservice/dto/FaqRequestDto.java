package com.buysellgo.helpdeskservice.dto;

import com.buysellgo.helpdeskservice.entity.Faq;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqRequestDto {
    private Long faqGroupId;
    private Timestamp createdAt;
    private String faqTitle;
    private String faqContent;

//    public Faq toEntity() {
//        return Faq.of(faqGroupId, faqTitle, faqContent)
//    }
}
