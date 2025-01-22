package com.buysellgo.promotionservice.controller;

import com.buysellgo.promotionservice.common.auth.TokenUserInfo;
import com.buysellgo.promotionservice.common.dto.CommonResDto;
import com.buysellgo.promotionservice.dto.CouponRequestDto;
import com.buysellgo.promotionservice.dto.IssuedCouponResponseDto;
import com.buysellgo.promotionservice.entity.Coupon;
import com.buysellgo.promotionservice.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
@Slf4j
public class CouponController {
    private final CouponService couponService;

    @Operation(summary = "쿠폰 등록(관리자)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createCoupon(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @Valid @RequestBody CouponRequestDto couponRequestDto) {

        // 인증된 사용자 정보 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        Coupon coupon = couponService.createCoupon(couponRequestDto);
        if (coupon == null) {
            return new ResponseEntity<>(
                    new CommonResDto<>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "쿠폰 생성 실패",
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto<Long> resDto = new CommonResDto<>(HttpStatus.CREATED,
                "쿠폰 생성 성공",
                coupon.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    @Operation(summary = "쿠폰 리스트 조회")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<?> listCoupon(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            Pageable pageable) {

        // 인증된 사용자 정보 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        Page<Coupon.Vo> couponList = couponService.getCouponList(pageable);

        if (couponList == null) {
            return new ResponseEntity<>(
                    new CommonResDto<>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "쿠폰 리스트 조회 실패",
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto<Page<Coupon.Vo>> resDto = new CommonResDto<>(HttpStatus.OK,
                "쿠폰 리스트 조회 성공",
                couponList);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @Operation(summary = "쿠폰 삭제")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCoupon(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @PathVariable Long id) {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        couponService.deleteCoupon(id);

        CommonResDto<Object> resDto = new CommonResDto<>(HttpStatus.OK,
                "쿠폰 삭제 성공",
                null);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @Operation(summary = "발급 쿠폰 조회")
    @GetMapping("/issued-list")
    public ResponseEntity<?> listIssuedCoupon(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            Pageable pageable) {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        Page<IssuedCouponResponseDto> issuedCouponList
                = couponService.getIssuedCouponList(tokenUserInfo.getId(), pageable);

        CommonResDto<Page<IssuedCouponResponseDto>> resDto = new CommonResDto<>(HttpStatus.OK, "발급된 쿠폰리스트 조회 성공", issuedCouponList);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
}
