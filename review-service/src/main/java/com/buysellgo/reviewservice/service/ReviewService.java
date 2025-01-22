package com.buysellgo.reviewservice.service;

import com.buysellgo.reviewservice.service.dto.ServiceResult;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewService {

    public ServiceResult<Map<String, Object>> getReviewGuest(long productId) {
        Map<String, Object> data = new HashMap<>();
        data.put("productId", productId);
        return ServiceResult.success("리뷰 조회 성공", data);
    }

}

