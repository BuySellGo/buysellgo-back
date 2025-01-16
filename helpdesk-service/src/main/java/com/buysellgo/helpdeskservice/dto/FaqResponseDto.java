package com.buysellgo.helpdeskservice.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqResponseDto {
    private Long faqId;
    private String faqTitle;
    private String faqContent;
    private Timestamp faqCreatedAt;
    private Long faqGroupId;
    private String faqGroupTitle;
}
