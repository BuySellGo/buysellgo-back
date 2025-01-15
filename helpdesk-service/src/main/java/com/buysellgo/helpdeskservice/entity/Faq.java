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
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_group_id")
    private FaqGroup faqGroup;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "faq_title", columnDefinition = "varchar(200)")
    private String faqTitle;

    @Column(name = "faq_content", columnDefinition = "TEXT")
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
        return new Vo(id, faqGroup.getId(), createdAt, faqTitle, faqContent);
    }

    private record Vo(Long id,
                      Long faqGroupId,
                      Timestamp createdAt,
                      String faqTitle,
                      String faqContent) {
    }
}
