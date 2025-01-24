package com.buysellgo.reviewservice.strategy.impl;

import com.buysellgo.reviewservice.strategy.common.ReviewStrategy;
import com.buysellgo.reviewservice.strategy.common.ReviewResult;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.buysellgo.reviewservice.common.entity.Role;
import com.buysellgo.reviewservice.controller.dto.ReviewCreateReq;
import java.util.Map;   
import com.buysellgo.reviewservice.repository.ReviewRepository;
import com.buysellgo.reviewservice.strategy.dto.ReviewDto;
import com.buysellgo.reviewservice.entity.Review;
import java.util.HashMap;
import java.util.Optional;
        
import static com.buysellgo.reviewservice.common.util.CommonConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional  
public class UserReviewStrategy implements ReviewStrategy<Map<String,Object>> {
    private final ReviewRepository reviewRepository;
    @Override
    public ReviewResult<Map<String, Object>> getReview(long userId) {
        return null;
    }

    @Override
    public ReviewResult<Map<String, Object>> writeReview(ReviewCreateReq req, long userId) {
        Map<String, Object> data = new HashMap<>();
        Review review = null;
        try{
            if(reviewRepository.existsByOrderId(req.orderId())){
                data.put(REVIEW_VO.getValue(), null);
                return ReviewResult.fail( "이미 작성한 리뷰가 있습니다.", data);
            }       
            ReviewDto reviewDto = ReviewDto.from(req, userId);
            review = Review.of(reviewDto.getUserId(), reviewDto.getProductId(), reviewDto.getSellerId(),reviewDto.getOrderId(), reviewDto.getStarRating(), reviewDto.getContent(), reviewDto.getImage());
            reviewRepository.save(review);
            data.put(REVIEW_VO.getValue(), review.toVo());
            return ReviewResult.success("리뷰 작성 완료", data);
        } catch (Exception e) {
            data.put(REVIEW_VO.getValue(), null);
            return ReviewResult.fail( "리뷰 작성 실패", data);
        }
    }

    @Override
    public ReviewResult<Map<String, Object>> updateReview(ReviewCreateReq req, long userId) {
        Map<String, Object> data = new HashMap<>();
        Review review = null;
        try{
            Optional<Review> reviewOptional = reviewRepository.findByOrderId(req.orderId());
            if(reviewOptional.isEmpty()){
                data.put(REVIEW_VO.getValue(), null);
                return ReviewResult.fail( "리뷰가 존재하지 않습니다.", data);
            }
            review = reviewOptional.get();
            review.setContent(req.content());
            review.setImage(req.imageUrl());
            reviewRepository.save(review);
            data.put(REVIEW_VO.getValue(), review.toVo());
            return ReviewResult.success("리뷰 수정 완료", data);
        } catch (Exception e) {
            data.put(REVIEW_VO.getValue(), null);
            return ReviewResult.fail( "리뷰 수정 실패", data);
        }
    }

    @Override
    public ReviewResult<Map<String, Object>> deleteReview(long reviewId) {
        return null;
    }

    @Override
    public ReviewResult<Map<String, Object>> activeReview(long reviewId) {
        return null;
    }

    @Override
    public ReviewResult inactiveReview(long reviewId) {
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.USER;
    }

}
