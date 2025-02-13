package com.buysellgo.reviewservice.strategy.common;

import com.buysellgo.reviewservice.common.entity.Role;
import com.buysellgo.reviewservice.controller.dto.ReviewCreateReq;

import java.util.Map;
/**
 * 리뷰 전략을 정의하는 인터페이스입니다.
 * 각 구현체는 리뷰 조회, 작성, 수정, 삭제 및 역할 지원 여부를 처리해야 합니다.
 *
 * @param <T> ReviewResult에 반환되는 데이터의 타입입니다.
 */
public interface ReviewStrategy<T extends Map<String, Object>> {
    /**
     * 역할에 따른 리뷰를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID입니다.
     * @return 리뷰 조회 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> getReview(long userId);

    /**
     * 리뷰를 작성합니다.
     *
     * @param req 리뷰 작성 요청 정보를 포함하는 ReviewCreateReq입니다.
     * @return 리뷰 작성 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> writeReview(ReviewCreateReq req, long userId);

    /**
     * 리뷰를 수정합니다.
     *
     * @param req 리뷰 수정 요청 정보를 포함하는 ReviewCreateReq입니다.
     * @return 리뷰 수정 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> updateReview(ReviewCreateReq req, long userId);

    /**
     * 리뷰를 삭제합니다.
     *
     * @param reviewId 삭제할 리뷰의 ID입니다.
     * @return 리뷰 삭제 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> deleteReview(long reviewId, long userId);
    /**
     * 주어진 역할을 이 전략이 지원하는지 여부를 결정합니다.
     *
     * @param role 확인할 역할입니다.
     * @return 지원 여부를 나타내는 boolean 값입니다.
     */
    boolean supports(Role role);
}


