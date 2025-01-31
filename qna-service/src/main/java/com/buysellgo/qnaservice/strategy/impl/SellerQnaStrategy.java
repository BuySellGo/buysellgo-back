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
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional  
public class SellerQnaStrategy implements QnaStrategy<Map<String, Object>> {
    private final QnaRepository qnaRepository;
    private final ReplyRepository replyRepository;


    @Override
    public QnaResult<Map<String, Object>> createQna(QnaReq req, long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put(QNA_VO.getValue(), "판매자는 Qna를 작성할 수 없습니다.");
        return QnaResult.fail(NOT_SUPPORTED.getValue(), data);
    }

    @Override
    public QnaResult<Map<String, Object>> getQna(long userId) {
        Map<String, Object> data = new HashMap<>();
        try{
            List<Qna> qnaList = qnaRepository.findBySellerId(userId);
            if(qnaList.isEmpty()){
                return QnaResult.fail(QNA_NOT_FOUND.getValue(), data);
            }
            data.put(QNA_VO.getValue(), qnaList.stream().map(Qna::toVo).toList());
            return QnaResult.success(QNA_LIST_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(QNA_VO.getValue(), e.getMessage());
            return QnaResult.fail(QNA_LIST_FAIL.getValue(), data);
        }
    }

    @Override
    public QnaResult<Map<String, Object>> updateQna(QnaReq req, long userId, long qnaId) {
        Map<String, Object> data = new HashMap<>();
        data.put(QNA_VO.getValue(), "판매자는 Qna를 수정할 수 없습니다.");
        return QnaResult.fail(NOT_SUPPORTED.getValue(), data);
    }

    @Override
    public QnaResult<Map<String, Object>> deleteQna(long qnaId, long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put(QNA_VO.getValue(), "판매자는 Qna를 삭제할 수 없습니다.");
        return QnaResult.fail(NOT_SUPPORTED.getValue(), data);
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
    public QnaResult<Map<String, Object>> updateReply(ReplyReq req, long userId, long replyId) {
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<QnaReply> replyOptional = replyRepository.findById(replyId);
            if(replyOptional.isEmpty()){
                return QnaResult.fail(REPLY_NOT_FOUND.getValue(), data);
            }
            QnaReply reply = replyOptional.get();
            if(reply.getSellerId() != userId){
                return QnaResult.fail(REPLY_UPDATE_PERMISSION_DENIED.getValue(), data);
            }
            ReplyDto replyDto = ReplyDto.from(req, userId);
            reply.setContent(replyDto.getContent());
            replyRepository.save(reply);
            data.put(REPLY_VO.getValue(), reply.toVo());
            return QnaResult.success(REPLY_UPDATE_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(REPLY_VO.getValue(), e.getMessage());
            return QnaResult.fail(REPLY_UPDATE_FAIL.getValue(), data);
        }
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.SELLER;
    }
}
