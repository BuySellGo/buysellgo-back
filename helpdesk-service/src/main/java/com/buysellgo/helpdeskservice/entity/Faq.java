package com.buysellgo.helpdeskservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "tbl_faq")
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long faqId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_group_id")
    private FaqGroup faqGroup;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "faq_title")
    private String faqTitle;

    @Column(name = "faq_content")
    private String faqContent;

    public static Faq of(FaqGroup faqGroup,
                         String faqTitle,
                         String faqContent) {
        return Faq.builder()
                .faqGroup(faqGroup)
                .createdAt(Timestamp.from(Instant.now()))
                .faqTitle(faqTitle)
                .faqContent(faqContent)
                .build();
    }

    public Vo toVo() {
        return new Vo(faqId, faqGroup, createdAt, faqTitle, faqContent);
    }

    private record Vo(Long faqId,
                      FaqGroup faqGroup,
                      Timestamp createdAt,
                      String faqTitle,
                      String faqContent) {
    }
}
