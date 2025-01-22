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
     * @param role 조회할 역할입니다.
     * @return 리뷰 조회 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> getReview(Role role);

    /**
     * 상품 ID에 따른 리뷰를 조회합니다. (비로그인 사용자용)
     *
     * @param productId 조회할 상품의 ID입니다.
     * @return 리뷰 조회 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> getReviewGuest(long productId);

    /**
     * 리뷰를 작성합니다.
     *
     * @param req 리뷰 작성 요청 정보를 포함하는 ReviewCreateReq입니다.
     * @return 리뷰 작성 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> writeReview(ReviewCreateReq req);

    /**
     * 리뷰를 수정합니다.
     *
     * @param req 리뷰 수정 요청 정보를 포함하는 ReviewCreateReq입니다.
     * @return 리뷰 수정 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> updateReview(ReviewCreateReq req);

    /**
     * 리뷰를 삭제합니다.
     *
     * @param reviewId 삭제할 리뷰의 ID입니다.
     * @return 리뷰 삭제 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> deleteReview(long reviewId);

    /**
     * 리뷰를 활성화합니다.
     *
     * @param reviewId 활성화할 리뷰의 ID입니다.
     * @return 리뷰 활성화 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> activeReview(long reviewId);

    /**
     * 리뷰를 비활성화합니다.
     *
     * @param reviewId 비활성화할 리뷰의 ID입니다.
     * @return 리뷰 비활성화 결과를 포함하는 ReviewResult입니다.
     */
    ReviewResult<T> inactiveReview(long reviewId);

    /**
     * 주어진 역할을 이 전략이 지원하는지 여부를 결정합니다.
     *
     * @param role 확인할 역할입니다.
     * @return 지원 여부를 나타내는 boolean 값입니다.
     */
    boolean supports(Role role);
}


