package com.buysellgo.qnaservice.strategy.dto;

import com.buysellgo.qnaservice.controller.dto.QnaReq;

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
public class QnaDto {
    private long userId;
    private long productId;
    private long sellerId;
    private boolean isPrivate;
    private String content;

    public static QnaDto from(QnaReq req, long userId){
        return QnaDto.builder()
                .userId(userId)
                .productId(req.productId())
                .sellerId(req.sellerId())
                .isPrivate(req.isPrivate())
                .content(req.content())
                .build();
    }
}
