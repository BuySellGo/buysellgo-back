package com.buysellgo.qnaservice.strategy.common;

import java.util.Map;
import com.buysellgo.qnaservice.common.entity.Role;
import com.buysellgo.qnaservice.controller.dto.QnaReq;
import com.buysellgo.qnaservice.controller.dto.ReplyReq;

public interface QnaStrategy<T extends Map<String, Object>> {
    /**
     * Qna를 생성합니다.
     *
     * @param req Qna 생성 요청 정보를 포함하는 QnaReq입니다.
     * @param userId 생성할 사용자의 ID입니다.
     * @return Qna 생성 결과를 포함하는 QnaResult입니다.
     */
    QnaResult<T> createQna(QnaReq req, long userId);

    /**
     * Qna를 수정합니다.
     *
     * @param req Qna 수정 요청 정보를 포함하는 QnaReq입니다.
     * @param userId 수정할 사용자의 ID입니다.
     * @param qnaId 수정할 Qna의 ID입니다.
     * @return Qna 수정 결과를 포함하는 QnaResult입니다.
     */
    QnaResult<T> updateQna(QnaReq req, long userId, long qnaId);

    /**
     * Qna를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID입니다.
     * @return Qna 조회 결과를 포함하는 QnaResult입니다.
     */
    QnaResult<T> getQna(long userId);

    /**
     * Qna를 삭제합니다.
     *
     * @param qnaId 삭제할 Qna의 ID입니다.
     * @param userId 삭제할 사용자의 ID입니다.
     * @return Qna 삭제 결과를 포함하는 QnaResult입니다.
     */
    QnaResult<T> deleteQna(long qnaId, long userId);

    /**
     * Qna 답변을 생성합니다.
     *
     * @param req 답변 생성 요청 정보를 포함하는 ReplyReq입니다.
     * @param userId 답변을 생성할 사용자의 ID입니다.
     * @return 답변 생성 결과를 포함하는 QnaResult입니다.
     */
    QnaResult<T> createReply(ReplyReq req, long userId);

    /**
     * Qna 답변을 수정합니다.
     *
     * @param req 답변 수정 요청 정보를 포함하는 ReplyReq입니다.
     * @param userId 답변을 수정할 사용자의 ID입니다.
     * @param replyId 수정할 답변의 ID입니다.
     * @return 답변 수정 결과를 포함하는 QnaResult입니다.
     */
    QnaResult<T> updateReply(ReplyReq req, long userId, long replyId);

    /**
     * 주어진 역할을 이 전략이 지원하는지 여부를 결정합니다.
     *
     * @param role 확인할 역할입니다.
     * @return 지원 여부를 나타내는 boolean 값입니다.
     */
    boolean supports(Role role);
}
