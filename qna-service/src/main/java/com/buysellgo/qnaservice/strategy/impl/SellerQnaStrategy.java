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
import java.util.HashMap;
import java.util.Optional;
import static com.buysellgo.qnaservice.common.util.CommonConstant.*;
import com.buysellgo.qnaservice.entity.Qna;
import com.buysellgo.qnaservice.entity.QnaReply;
import com.buysellgo.qnaservice.repository.QnaRepository;
import com.buysellgo.qnaservice.repository.ReplyRepository;
import com.buysellgo.qnaservice.strategy.dto.ReplyDto;
@Component
@RequiredArgsConstructor
@Slf4j
@Transactional  
public class SellerQnaStrategy implements QnaStrategy<Map<String, Object>> {
    private final QnaRepository qnaRepository;
    private final ReplyRepository replyRepository;


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
        Map<String, Object> data = new HashMap<>();
        try{
            ReplyDto replyDto = ReplyDto.from(req, userId);
            Optional<Qna> qna = qnaRepository.findById(replyDto.getQnaId());
            if(qna.isEmpty()){  
                return QnaResult.fail(QNA_NOT_FOUND.getValue(), data);
            }
            if(qna.get().getReply() != null){
                return QnaResult.fail(REPLY_ALREADY_EXISTS.getValue(), data);
            }
            QnaReply qnaReply = QnaReply.of(qna.get(),replyDto.getSellerId(), replyDto.getContent());
            replyRepository.save(qnaReply);
            data.put(REPLY_VO.getValue(), qnaReply.toVo());
            return QnaResult.success(REPLY_CREATE_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(REPLY_VO.getValue(), e.getMessage());
            return QnaResult.fail(REPLY_CREATE_FAIL.getValue(), data);
        }
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
