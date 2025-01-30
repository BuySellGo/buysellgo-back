package com.buysellgo.qnaservice.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.buysellgo.qnaservice.repository.QnaRepository;
import com.buysellgo.qnaservice.repository.ReplyRepository;
import com.buysellgo.qnaservice.entity.Qna;
import com.buysellgo.qnaservice.entity.QnaReply;
import com.buysellgo.qnaservice.service.dto.ServiceResult;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QnaService {

    private final QnaRepository qnaRepository;
    private final ReplyRepository replyRepository;

    public ServiceResult<Map<String, Object>> getQnaGuest(){
        //프로덕트 아이디 받아서 상품에 관한 뷰엔에이 문답 조회(프라이베이트한 큐엔에이 아니라면)
        return null;
    }
}
