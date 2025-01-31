package com.buysellgo.qnaservice.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.buysellgo.qnaservice.repository.QnaRepository;   
import com.buysellgo.qnaservice.entity.Qna;
import com.buysellgo.qnaservice.service.dto.ServiceResult;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.HashMap;
import static com.buysellgo.qnaservice.common.util.CommonConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QnaService {

    private final QnaRepository qnaRepository;

    public ServiceResult<Map<String, Object>> getQnaGuest(long productId){
        //프로덕트 아이디 받아서 상품에 관한 큐엔에이 문답 조회(프라이베이트한 큐엔에이 아니라면)
        Map<String, Object> data = new HashMap<>();
        List<Qna> qnaList = qnaRepository.findByProductId(productId);

        if(qnaList.isEmpty()){
            data.put(QNA_VO.getValue(), null);
            return ServiceResult.fail(QNA_NOT_FOUND.getValue(), data);
        }
        data.put(QNA_VO.getValue(), qnaList.stream().map(Qna::toVoGuest).toList());
        return ServiceResult.success(QNA_LIST_SUCCESS.getValue(), data);
    }
}

