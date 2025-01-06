package com.buysellgo.helpdeskservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "tbl_onetoone_inquiry_reply")
public class OneToOneInquiryReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onetoone_inquiry_reply_id")
    private Long onetooneInquiryReplyId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onetoone_inquiry_id", nullable = false)
    private OneToOneInquiry oneToOneInquiry;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public static OneToOneInquiryReply of(OneToOneInquiry oneToOneInquiry,
                                          Long userId,
                                          String content) {
        return OneToOneInquiryReply.builder()
                .oneToOneInquiry(oneToOneInquiry)
                .userId(userId)
                .content(content)
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .build();
    }

    public Vo toVo() {
        return new Vo(onetooneInquiryReplyId, oneToOneInquiry, userId, content, createdAt, updatedAt);
    }


    private record Vo(Long onetooneInquiryReplyId,
                      OneToOneInquiry oneToOneInquiry,
                      Long userId,
                      String content,
                      Timestamp createdAt,
                      Timestamp updatedAt) {
    }
}
