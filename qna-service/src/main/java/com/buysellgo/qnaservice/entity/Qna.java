package com.buysellgo.qnaservice.entity;

import com.buysellgo.qnaservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_qna")
public class Qna extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long qnaId;

    @Column(name = "userId", columnDefinition = "bigint", nullable = false, unique = false)
    private long userId;

    @Column(name = "sellerId", columnDefinition = "bigint", nullable = false, unique = false)
    private long sellerId;

    @Column(name = "isPrivate", columnDefinition = "boolean", nullable = false, unique = false)
    private boolean isPrivate;

    @Column(name = "content", columnDefinition = "text", nullable = false, unique = false)
    private String content;

    @OneToOne(mappedBy = "qna", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private QnaReply reply;

    public static Qna of(long userId, long sellerId, boolean isPrivate, String content) {
        return Qna.builder()
                .userId(userId)
                .sellerId(sellerId)
                .isPrivate(isPrivate)
                .content(content)
                .build();
    }

    public Vo toVo(){
        return new Vo(qnaId, userId, sellerId, isPrivate, content, version, createdAt, updatedAt);
    }

    public record Vo(
            long qnaId,
            long userId,
            long sellerId,
            boolean isPrivate,
            String content,
            long version,
            Timestamp createdAt,
            Timestamp updatedAt
    ){}
}
