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
@Table(name = "tbl_qnareply")
public class QnaReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_reply_id", columnDefinition = "bigint", nullable = false, unique = true)
    private long qnaReplyId;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id", columnDefinition = "bigint", nullable = false, unique = true)
    private Qna qna;

    @Column(name = "seller_id", columnDefinition = "bigint", nullable = false, unique = false)
    private long sellerId;

    @Column(name = "content", columnDefinition = "text", nullable = false, unique = false)
    private String content;

    public static QnaReply of(Qna qna, long sellerId, String content) {
        return QnaReply.builder()
                .qna(qna)
                .sellerId(sellerId)
                .content(content)
                .build();
    }

    public Vo toVo(){
        return new Vo(qnaReplyId, qna.getQnaId(), sellerId, content, version, createdAt, updatedAt);
    }

    public record Vo(
            long qnaReplyId,
            long qnaId,
            long sellerId,
            String content,
            long version,
            Timestamp createdAt,
            Timestamp updatedAt
    ){}
}
