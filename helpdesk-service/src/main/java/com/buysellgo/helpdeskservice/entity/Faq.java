package com.buysellgo.helpdeskservice.entity;

import com.buysellgo.helpdeskservice.dto.FaqRequestDto;
import com.buysellgo.helpdeskservice.dto.NoticeRequestDto;
import com.buysellgo.helpdeskservice.repository.FaqGroupRepository;
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

    public record Vo(Long id,
                     Long faqGroupId,
                     Timestamp createdAt,
                     String faqTitle,
                     String faqContent) {
    }

    public void update(FaqRequestDto faqRequestDto, FaqGroup newFaqGroup) {

        this.faqTitle = faqRequestDto.getFaqTitle();
        this.faqContent = faqRequestDto.getFaqContent();
        this.faqGroup = newFaqGroup;

    }
}
