package com.buysellgo.reviewservice.controller;

import java.util.Map;

import com.buysellgo.reviewservice.common.exception.CustomException;
import com.buysellgo.reviewservice.service.ReviewService;
import com.buysellgo.reviewservice.service.dto.ServiceResult;
import com.buysellgo.reviewservice.strategy.common.ReviewResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.buysellgo.reviewservice.common.auth.JwtTokenProvider;
import com.buysellgo.reviewservice.common.auth.TokenUserInfo;
import com.buysellgo.reviewservice.common.dto.CommonResDto;

import com.buysellgo.reviewservice.strategy.common.ReviewContext;
import com.buysellgo.reviewservice.strategy.common.ReviewStrategy;
import com.buysellgo.reviewservice.controller.dto.ReviewCreateReq;
        
import jakarta.validation.Valid;
import static com.buysellgo.reviewservice.common.util.CommonConstant.*;


@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewContext reviewContext;
    private final JwtTokenProvider jwtTokenProvider;
    private final ReviewService reviewService;

    @Operation(summary ="리뷰 작성(회원)")
    @PostMapping("/write")
    public ResponseEntity<CommonResDto<Map<String, Object>>> writeReview(@RequestHeader("Authorization") String accessToken,@Valid @RequestBody ReviewCreateReq req) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new CustomException(INVALID_TOKEN.getValue());
        }

        // 토큰에서 사용자 정보 추출
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);

        ReviewStrategy<Map<String, Object>> strategy = reviewContext.getStrategy(userInfo.getRole());
        ReviewResult<Map<String, Object>> result = strategy.writeReview(req);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "리뷰 작성 완료", null));
    }

    @Operation(summary ="리뷰 조회(회원,판매자,관리자)")
    @GetMapping("/list/role")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getReview(@RequestHeader("Authorization") String accessToken ) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new CustomException(INVALID_TOKEN.getValue());
        }

        // 토큰에서 사용자 정보 추출
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        ReviewStrategy<Map<String, Object>> strategy = reviewContext.getStrategy(userInfo.getRole());
        ReviewResult<Map<String, Object>> result = strategy.getReview(userInfo.getRole());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "리뷰 조회 완료", null));
    }

    @Operation(summary ="리뷰 조회(상품별, 로그인 안한 유저도 조회 가능)")
    @GetMapping("/list/guest")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getReviewGuest(@RequestParam long sellerId) {
        ServiceResult<Map<String, Object>> result = reviewService.getReviewGuest(sellerId);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "리뷰 조회 완료", null));
    }

    @Operation(summary ="리뷰 수정(회원)")
    @PutMapping("/update")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateReview(@RequestHeader("Authorization") String accessToken, @RequestBody ReviewCreateReq req) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new CustomException(INVALID_TOKEN.getValue());
        }

        // 토큰에서 사용자 정보 추출
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        ReviewStrategy<Map<String, Object>> strategy = reviewContext.getStrategy(userInfo.getRole());
        ReviewResult<Map<String, Object>> result = strategy.updateReview(req);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "리뷰 수정 완료", null));
    }

    @Operation(summary ="리뷰 삭제(회원,관리자)")
    @DeleteMapping("/delete")
    public ResponseEntity<CommonResDto<Map<String, Object>>> deleteReview(@RequestHeader("Authorization") String accessToken, @RequestParam long reviewId) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new CustomException(INVALID_TOKEN.getValue());
        }

        // 토큰에서 사용자 정보 추출
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        ReviewStrategy<Map<String, Object>> strategy = reviewContext.getStrategy(userInfo.getRole());
        ReviewResult<Map<String, Object>> result = strategy.deleteReview(reviewId);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "리뷰 삭제 완료", null));
    }

    @Operation(summary = "리뷰 활성화(관리자)")
    @PutMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResDto<Map<String, Object>>> activeReview(@RequestHeader("Authorization") String accessToken, @RequestParam long reviewId) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new CustomException(INVALID_TOKEN.getValue());
        }
        // 토큰에서 사용자 정보 추출
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        ReviewStrategy<Map<String, Object>> strategy = reviewContext.getStrategy(userInfo.getRole());
        ReviewResult<Map<String, Object>> result = strategy.activeReview(reviewId);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "리뷰 활성화 완료", null));
    }

    @Operation(summary = "리뷰 비활성화(관리자)")
    @PutMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResDto<Map<String, Object>>> inactiveReview(@RequestHeader("Authorization") String accessToken, @RequestParam long reviewId) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new CustomException(INVALID_TOKEN.getValue());
        }
        // 토큰에서 사용자 정보 추출
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        ReviewStrategy<Map<String, Object>> strategy = reviewContext.getStrategy(userInfo.getRole());
        ReviewResult<Map<String, Object>> result = strategy.inactiveReview(reviewId);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "리뷰 비활성화 완료", null));
    }
}

