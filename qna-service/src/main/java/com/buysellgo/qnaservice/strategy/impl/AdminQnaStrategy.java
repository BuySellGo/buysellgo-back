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
import java.util.List;
import java.util.HashMap;
import com.buysellgo.qnaservice.entity.Qna;
import com.buysellgo.qnaservice.repository.QnaRepository;
import static com.buysellgo.qnaservice.common.util.CommonConstant.*;
@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminQnaStrategy implements QnaStrategy<Map<String, Object>> {
    private final QnaRepository qnaRepository;

    @Override
    public QnaResult<Map<String, Object>> createQna(QnaReq req, long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put(QNA_VO.getValue(), "Admin은 Qna를 작성할 수 없습니다.");
        return QnaResult.fail(NOT_SUPPORTED.getValue(), data);
    }

    @Override
    public QnaResult<Map<String, Object>> getQna(long userId) {
        Map<String, Object> data = new HashMap<>();
        try{
            List<Qna> qnaList = qnaRepository.findAll();
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
        data.put(QNA_VO.getValue(), "Admin은 Qna를 수정할 수 없습니다.");
        return QnaResult.fail(NOT_SUPPORTED.getValue(), data);
    }

    @Override
    public QnaResult<Map<String, Object>> deleteQna(long qnaId, long userId) {
        Map<String, Object> data = new HashMap<>();
        try{
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
        data.put(REPLY_VO.getValue(), "Admin은 답변을 작성할 수 없습니다.");
        return QnaResult.fail(NOT_SUPPORTED.getValue(), data);
    }

    @Override
    public QnaResult<Map<String, Object>> updateReply(ReplyReq req, long userId, long replyId) {
        Map<String, Object> data = new HashMap<>();
        data.put(REPLY_VO.getValue(), "Admin은 답변을 수정할 수 없습니다.");
        return QnaResult.fail(NOT_SUPPORTED.getValue(), data);
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.ADMIN;
    }
}
