package com.buysellgo.reviewservice.strategy.impl;

import com.buysellgo.reviewservice.strategy.common.ReviewStrategy;
import java.util.Map;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.buysellgo.reviewservice.common.entity.Role;
import com.buysellgo.reviewservice.controller.dto.ReviewCreateReq;
import com.buysellgo.reviewservice.strategy.common.ReviewResult;
import com.buysellgo.reviewservice.entity.Review;
import com.buysellgo.reviewservice.repository.ReviewRepository;
import java.util.HashMap;
import java.util.List;
import static com.buysellgo.reviewservice.common.util.CommonConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional  
public class AdminReviewStrategy implements ReviewStrategy<Map<String, Object>> {
    private final ReviewRepository reviewRepository;
    @Override
    public ReviewResult<Map<String, Object>> getReview(long userId) { 
        Map<String, Object> data = new HashMap<>();
        List<Review> reviews = reviewRepository.findAll();
        if(reviews.isEmpty()){
            data.put(REVIEW_VO.getValue(), null);
            return ReviewResult.fail("리뷰가 존재하지 않습니다.", data);
        }
        List<Review.Vo> reviewVos = reviews.stream().map(Review::toVo).toList();
        data.put(REVIEW_VO.getValue(), reviewVos);
        return ReviewResult.success("모든 리뷰 조회 완료", data);
    }

    @Override
    public ReviewResult<Map<String, Object>> writeReview(ReviewCreateReq req, long userId) {
        // 리뷰 작성 권한은 회원만 함
        return null;
    }

    @Override
    public ReviewResult<Map<String, Object>> updateReview(ReviewCreateReq req, long userId) {
        // 리뷰 수정 권한은 회원과 관리자만 함
        return null;
    }

    @Override
    public ReviewResult<Map<String, Object>> deleteReview(long reviewId) {
        // 리뷰 삭제 권한은 회원과 관리자만 함
        return null;
    }

    @Override
    public ReviewResult<Map<String, Object>> activeReview(long reviewId) {
        // 리뷰 활성화 권한은 관리자만 함
        return null;
    }

    @Override
    public ReviewResult<Map<String, Object>> inactiveReview(long reviewId) {
        // 리뷰 비활성화 권한은 관리자만 함
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.ADMIN;      
    }

}
