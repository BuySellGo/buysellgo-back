package com.buysellgo.qnaservice.strategy.impl;

import org.springframework.stereotype.Component;
import com.buysellgo.qnaservice.strategy.common.QnaStrategy;
import java.util.Map;
import com.buysellgo.qnaservice.common.entity.Role;
import com.buysellgo.qnaservice.controller.dto.QnaReq;
import com.buysellgo.qnaservice.controller.dto.ReplyReq;
import com.buysellgo.qnaservice.strategy.common.QnaResult;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional  
public class SellerQnaStrategy implements QnaStrategy<Map<String, Object>> {

    @Override
    public QnaResult<Map<String, Object>> createQna(QnaReq req, long userId) {
        return null;
    }

    @Override
    public QnaResult<Map<String, Object>> getQna(long userId) {
        return null;
    }

    @Override
    public QnaResult<Map<String, Object>> updateQna(QnaReq req, long userId) {
        return null;
    }

    @Override
    public QnaResult<Map<String, Object>> deleteQna(long qnaId, long userId) {
        return null;
    }

    @Override
    public QnaResult<Map<String, Object>> createReply(ReplyReq req, long userId) {
        return null;
    }

    @Override
    public QnaResult<Map<String, Object>> updateReply(ReplyReq req, long userId) {
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.SELLER;
    }
}
