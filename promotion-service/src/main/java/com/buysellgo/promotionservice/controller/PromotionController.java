package com.buysellgo.promotionservice.controller;

import com.buysellgo.promotionservice.common.auth.TokenUserInfo;
import com.buysellgo.promotionservice.common.dto.CommonResDto;
import com.buysellgo.promotionservice.dto.PromotionRequestDto;
import com.buysellgo.promotionservice.entity.Promotion;
import com.buysellgo.promotionservice.service.PromotionService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotion")
@RequiredArgsConstructor
@Slf4j
public class PromotionController {
    private final PromotionService promotionService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "광고(프로모션) 요청(Seller -> Admin)")
    @PostMapping("/request")
    public ResponseEntity<?> createPromotionRequest(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @Valid @RequestBody PromotionRequestDto promotionRequestDto) {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        Promotion promotion =
                promotionService.createPromotion(tokenUserInfo.getId(), promotionRequestDto);

        if (promotion == null) {
            return new ResponseEntity<>(
                    new CommonResDto<>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "프로모션 요청 생성 실패",
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto<Long> resDto =
                new CommonResDto<>(HttpStatus.CREATED,
                        "프로모션 요청 생성 성공",
                        promotion.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "광고(프로모션) 요청 리스트 조회")
    @GetMapping("/list")
    public ResponseEntity<?> listPromotions(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            Pageable pageable) {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        Page<Promotion.Vo> promotionList = promotionService.getPromotionList(pageable);

        if (promotionList == null) {
            return new ResponseEntity<>(
                    new CommonResDto<>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "프로모션 요청 리스트 조회 실패",
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto<Page<Promotion.Vo>> resDto = new CommonResDto<>(HttpStatus.OK, "프로모션 요청 리스트 조회 성공", promotionList);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "광고(프로모션) 요청 승인(관리자)")
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approvePromotion(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @PathVariable Long id, @RequestParam Boolean isApproved) {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        Promotion.Vo vo = promotionService.approvePromotion(id, isApproved);

        CommonResDto<Long> resDto = new CommonResDto<>(HttpStatus.OK, "광고(프로모션) 요청 승인(관리자) 성공", id);

        return new ResponseEntity<>(resDto, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "광고(프로모션) 요청 삭제")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePromotion(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @PathVariable Long id) {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        promotionService.deletePromotion(id);

        CommonResDto<Object> resDto = new CommonResDto<>(HttpStatus.OK, "광고(프로모션) 요청 삭제 성공", null);

        return new ResponseEntity<>(resDto, HttpStatus.OK);

    }
}
