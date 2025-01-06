package com.buysellgo.helpdeskservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "user_id")
    private Long userId;  // 단순 필드 참조

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "title", columnDefinition = "varchar(200)")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    public static Notice of(Long userId,
                            String title,
                            String content) {
        return Notice.builder()
                .userId(userId)
                .createdAt(Timestamp.from(Instant.now()))
                .startDate(Timestamp.from(Instant.now()))
                .endDate(Timestamp.from(Instant.now().plus(Duration.ofDays(3))))
                .title(title)
                .content(content)
                .build();
    }

    public Vo toVo() {
        return new Vo(noticeId, userId, createdAt, startDate, endDate, title, content);
    }

    private record Vo(Long noticeId,
                      Long userId,
                      Timestamp createdAt,
                      Timestamp startDate,
                      Timestamp endDate,
                      String title,
                      String content) {
    }
}

/*
CREATE TABLE `notice` (
        `notice_id`	bigint auto_increment primary key	NOT NULL,
	`user_id`	bigint	NULL,
	`created_at`	datetime default current_timestamp	NULL,
	`start_date`	datetime	NULL,
	`end_date`	datetime	NULL,
	`title`	varchar(200)	NULL,
	`content`	text	NULL
);

 */