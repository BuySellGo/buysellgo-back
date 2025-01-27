package com.buysellgo.reviewservice.service;

import com.buysellgo.reviewservice.service.dto.ServiceResult;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import static com.buysellgo.reviewservice.common.util.CommonConstant.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import com.buysellgo.reviewservice.repository.ReviewRepository;
import com.buysellgo.reviewservice.entity.Review;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ServiceResult<Map<String, Object>> getReviewGuest(long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        if(reviews.isEmpty()){
            return ServiceResult.fail(REVIEW_NOT_FOUND.getValue(), null);
        }   
        List<Review.Vo> reviewVos = reviews.stream().map(Review::toVo).toList();
        Map<String, Object> data = new HashMap<>();
        data.put("reviews", reviewVos); 
        return ServiceResult.success(REVIEW_GET_SUCCESS.getValue(), data);
    }

}

