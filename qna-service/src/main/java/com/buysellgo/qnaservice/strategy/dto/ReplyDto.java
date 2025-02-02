package com.buysellgo.qnaservice.strategy.dto;

import com.buysellgo.qnaservice.controller.dto.ReplyReq;

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
public class ReplyDto {
    private long qnaId;
    private long sellerId;
    private String content;

    public static ReplyDto from(ReplyReq req, long sellerId){
        return ReplyDto.builder()
                .qnaId(req.qnaId())
                .sellerId(sellerId)
                .content(req.content())
                .build();
    }
}
