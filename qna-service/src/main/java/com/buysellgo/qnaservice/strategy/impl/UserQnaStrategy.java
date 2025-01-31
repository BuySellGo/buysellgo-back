package com.buysellgo.qnaservice.strategy.impl;

import org.springframework.stereotype.Component;

import com.buysellgo.qnaservice.strategy.common.QnaStrategy;
import com.buysellgo.qnaservice.common.entity.Role;
import com.buysellgo.qnaservice.strategy.common.QnaResult;
import com.buysellgo.qnaservice.controller.dto.QnaReq;
import com.buysellgo.qnaservice.controller.dto.ReplyReq;

import java.util.Map;   
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.buysellgo.qnaservice.entity.Qna;
import com.buysellgo.qnaservice.strategy.dto.QnaDto;
import com.buysellgo.qnaservice.repository.QnaRepository;
import java.util.HashMap;
import static com.buysellgo.qnaservice.common.util.CommonConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserQnaStrategy implements QnaStrategy<Map<String, Object>> {
    private final QnaRepository qnaRepository;

    @Override
    public QnaResult<Map<String, Object>> createQna(QnaReq req, long userId) {
        Map<String, Object> data = new HashMap<>();
        try{
            QnaDto qnaDto = QnaDto.from(req, userId);
            Qna qna = Qna.of(qnaDto.getUserId(), qnaDto.getProductId(), qnaDto.getSellerId(),qnaDto.isPrivate(), qnaDto.getContent()); 
            qnaRepository.save(qna);
            data.put(QNA_VO.getValue(), qna.toVo());
            return QnaResult.success(QNA_CREATE_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(QNA_VO.getValue(), e.getMessage());
            return QnaResult.fail(QNA_CREATE_FAIL.getValue(), data);
        }
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
        return role == Role.USER;
    }
}
