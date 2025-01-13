package com.buysellgo.helpdeskservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name="tbl_faq_group")
public class FaqGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "faq_group_title", columnDefinition = "varchar(100)")
    private String faqGroupTitle;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public static FaqGroup of(String title){
        return FaqGroup.builder()
                .createdAt(Timestamp.from(Instant.now()))
                .build();
    }

    public Vo toVo(){
        return new Vo(id, createdAt, faqGroupTitle);
    }

    private record Vo(Long id,
                      Timestamp createdAt,
                      String faqGroupTitle) {

    }
}

/*
CREATE TABLE `faq_group` (
	`faq_group_id`	bigint auto_increment primary key	NOT NULL,
	`faq_group_title`	varchar(100)	NULL,
	`created_at`	datetime default current_timestamp	NULL
);
 */