package com.buysellgo.reviewservice.entity;

import com.buysellgo.reviewservice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reviewId;

    @Column(name = "user_id", columnDefinition = "bigint", nullable = false, unique = false)
    private long userId;

    @Column(name = "product_id", columnDefinition = "bigint", nullable = false, unique = false)
    private long productId;

    @Column(name = "seller_id", columnDefinition = "bigint", nullable = false, unique = false)
    private long sellerId;

    @Column(name = "order_id", columnDefinition = "bigint", nullable = false, unique = true)
    private long orderId;

    @Column(name = "star_rating", columnDefinition = "int", nullable = false, unique = false)
    private int starRating;

    @Column(name = "content", columnDefinition = "text", nullable = false, unique = false)
    private String content;

    @Column(name = "image", columnDefinition = "varchar(200)", nullable = true, unique = false)
    private String image;

    public static Review of(long userId, long productId, long sellerId, long orderId, int starRating, String content, String image) {
        return Review.builder()
                .userId(userId)
                .productId(productId)
                .sellerId(sellerId)
                .orderId(orderId)
                .starRating(starRating)
                .content(content)
                .image(image)
                .build();
    }

    public Vo toVo(){
        return new Vo(reviewId, userId, productId, sellerId, starRating, content, image, version, createdAt, updatedAt);
    }

    public record Vo(
            long reviewId,
            long userId,
            long productId,
            long sellerId,
            int starRating,
            String content,
            String image,
            long version,
            Timestamp createdAt,
            Timestamp updatedAt
    ){}
}
