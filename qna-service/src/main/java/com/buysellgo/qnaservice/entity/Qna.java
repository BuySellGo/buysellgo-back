package com.buysellgo.qnaservice.entity;

import com.buysellgo.qnaservice.common.entity.BaseEntity;

import io.netty.util.internal.ObjectUtil;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

import org.apache.commons.lang3.ObjectUtils;

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

    @Column(name = "productId", columnDefinition = "bigint", nullable = false, unique = false)
    private long productId;

    @Column(name = "sellerId", columnDefinition = "bigint", nullable = false, unique = false)
    private long sellerId;

    @Column(name = "isPrivate", columnDefinition = "boolean", nullable = false, unique = false)
    private boolean isPrivate;

    @Column(name = "content", columnDefinition = "text", nullable = false, unique = false)
    private String content;

    @OneToOne(mappedBy = "qna", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private QnaReply reply;

    public static Qna of(long userId, long productId, long sellerId, boolean isPrivate, String content) {
        return Qna.builder()
                .userId(userId)
                .productId(productId)
                .sellerId(sellerId)
                .isPrivate(isPrivate)
                .content(content)
                .build();
    }

    public Vo toVo(){
        return new Vo(qnaId, userId, productId, sellerId, isPrivate, content, getObject(), version, createdAt, updatedAt);
    }

    public Vo toVoGuest(){
        return new Vo(qnaId, userId, productId, sellerId, isPrivate,isPrivate ? "비공개" : content ,isPrivate ? "비공개" : getObject(), version, createdAt, updatedAt);
    }

    private Object getObject() {
        return ObjectUtils.isNotEmpty(reply) ? reply.toVo() : "reply not found";
    }

    public record Vo(
            long qnaId,
            long userId,
            long productId,
            long sellerId,
            boolean isPrivate,
            String content,
            Object reply,
            long version,
            Timestamp createdAt,
            Timestamp updatedAt
    ){}
}
