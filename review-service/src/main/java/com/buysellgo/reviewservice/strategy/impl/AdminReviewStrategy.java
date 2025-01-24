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

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional  
public class AdminReviewStrategy implements ReviewStrategy<Map<String, Object>> {

    @Override
    public ReviewResult<Map<String, Object>> getReview(long userId) { 
        // 관리자의 경우 모든 리뷰를 조회해야 함
        return null;
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
