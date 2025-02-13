package com.buysellgo.reviewservice.strategy.dto;

import com.buysellgo.reviewservice.controller.dto.ReviewCreateReq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private long userId;
    private long productId;
    private long sellerId;
    private long orderId;
    private int starRating;
    private String content;
    private String image;

    public static ReviewDto from(ReviewCreateReq req, long userId){
        return ReviewDto.builder()
                .userId(userId)
                .productId(req.productId())
                .sellerId(req.sellerId())
                .orderId(req.orderId())
                .starRating(req.starRating())
                .content(req.content())
                .image(req.imageUrl())
                .build();
    }
}
