package com.buysellgo.helpdeskservice.entity;

import com.buysellgo.helpdeskservice.dto.NoticeRequestDto;
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
    @Column(name = "id")
    private Long id;

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
                            Timestamp startDate,
                            Timestamp endDate,
                            String content) {
        return Notice.builder()
                .userId(userId)
                .createdAt(Timestamp.from(Instant.now()))
//                .startDate(Timestamp.from(Instant.now()))
                .startDate(startDate)
//                .endDate(Timestamp.from(Instant.now().plus(Duration.ofDays(3))))
                .endDate(endDate)
                .title(title)
                .content(content)
                .build();
    }

    public Vo toVo() {

        return new Vo(id, userId, createdAt, startDate, endDate, title, content);
    }

    public record Vo(Long id,
                      Long userId,
                      Timestamp createdAt,
                      Timestamp startDate,
                      Timestamp endDate,
                      String title,
                      String content) {
    }

    public void update(NoticeRequestDto noticeRequestDto, long userId) {
        this.title = noticeRequestDto.getTitle();
        this.content = noticeRequestDto.getContent();
        this.startDate = noticeRequestDto.getStartDate();
        this.endDate = noticeRequestDto.getEndDate();
        this.userId = userId;
    }
}
