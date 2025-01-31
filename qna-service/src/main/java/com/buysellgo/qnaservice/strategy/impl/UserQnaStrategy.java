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
import java.util.List;
import java.util.Optional;
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
        Map<String, Object> data = new HashMap<>();
        try{
            List<Qna> qnaList = qnaRepository.findByUserId(userId);
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
        try{
            Optional<Qna> qnaOptional = qnaRepository.findById(qnaId);
            if(qnaOptional.isEmpty()){
                return QnaResult.fail(QNA_NOT_FOUND.getValue(), data);
            }
            Qna qna = qnaOptional.get();
            if(qna.getUserId() != userId){
                return QnaResult.fail(QNA_UPDATE_PERMISSION_DENIED.getValue(), data);
            }
            QnaDto qnaDto = QnaDto.from(req, userId);
            qna.setPrivate(qnaDto.isPrivate());
            qna.setContent(qnaDto.getContent());
            qnaRepository.save(qna);
            data.put(QNA_VO.getValue(), qna.toVo());
            return QnaResult.success(QNA_UPDATE_SUCCESS.getValue(), data);  
        } catch (Exception e) {
            data.put(QNA_VO.getValue(), e.getMessage());
            return QnaResult.fail(QNA_UPDATE_FAIL.getValue(), data);
        }
    }

    @Override
    public QnaResult<Map<String, Object>> deleteQna(long qnaId, long userId) {
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Qna> qna = qnaRepository.findById(qnaId);
            if(qna.isEmpty()){
                return QnaResult.fail(QNA_NOT_FOUND.getValue(), data);
            }
            if(qna.get().getUserId() != userId){
                return QnaResult.fail(QNA_DELETE_PERMISSION_DENIED.getValue(), data);
            }
            qnaRepository.deleteById(qnaId);
            return QnaResult.success(QNA_DELETE_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(QNA_VO.getValue(), e.getMessage());
            return QnaResult.fail(QNA_DELETE_FAIL.getValue(), data);
        }
    }   

    @Override
    public QnaResult<Map<String, Object>> createReply(ReplyReq req, long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put(REPLY_VO.getValue(), "회원은 답변을 작성할 수 없습니다.");
        return QnaResult.fail(NOT_SUPPORTED.getValue(), data);
    }

    @Override
    public QnaResult<Map<String, Object>> updateReply(ReplyReq req, long userId, long replyId) {
        Map<String, Object> data = new HashMap<>();
        data.put(REPLY_VO.getValue(), "회원은 답변을 수정할 수 없습니다.");
        return QnaResult.fail(NOT_SUPPORTED.getValue(), data);
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.USER;
    }
}
