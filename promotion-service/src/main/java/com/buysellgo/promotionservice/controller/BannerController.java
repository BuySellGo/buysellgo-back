package com.buysellgo.promotionservice.controller;

import com.buysellgo.promotionservice.common.auth.TokenUserInfo;
import com.buysellgo.promotionservice.common.dto.CommonResDto;
import com.buysellgo.promotionservice.dto.BannerRequestDto;
import com.buysellgo.promotionservice.entity.Banners;
import com.buysellgo.promotionservice.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1/banner")
@RequiredArgsConstructor
@Slf4j
public class BannerController {
    private final BannerService bannerService;

    @Operation(summary = "배너 등록(관리자)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createBanner(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @Valid @RequestPart("bannerRequestDto") BannerRequestDto bannerRequestDto,
            @RequestPart("bannerImagePath") MultipartFile bannerImagePath) throws IOException {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        System.out.println("프로모션 ID: " + bannerRequestDto.getPromotionId());
        System.out.println("배너 제목: " + bannerRequestDto.getBannerTitle());
        System.out.println("배너 이미지 파일 이름: " + bannerImagePath.getOriginalFilename());
        System.out.println("배너 URL: " + bannerRequestDto.getProductUrl());
        System.out.println("시작 시간: " + bannerRequestDto.getStartDate());
        System.out.println("종료 시간: " + bannerRequestDto.getEndDate());

        log.info("createBanner: {}", bannerRequestDto);
        Banners banner = bannerService.createBanner(bannerRequestDto, bannerImagePath);

        // 배너 생성 실패 시 처리
        if (banner == null) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "배너 생성 실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "배너 생성 성공", banner.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "배너 리스트 조회(관리자)")
    @GetMapping("/list")
    public ResponseEntity<?> listBanners(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            Pageable pageable) {
//  URL: {{local-gateway}}/promotion-service/api/v1/banner/list?page=1&size=4&sort=id

        Page<Banners.Vo> bannerList = bannerService.getBannerList(pageable);

        CommonResDto<Page<Banners.Vo>> resDto = new CommonResDto<>(HttpStatus.OK, "배너 리스트 조회 성공", bannerList);

        return new ResponseEntity<>(resDto, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "배너 활성화(관리자)")
    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateBanner(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @PathVariable Long id,
            @RequestParam Boolean isActivated) {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        Banners.Vo vo = bannerService.activateBanner(id, isActivated);

        CommonResDto<Long> resDto = new CommonResDto<>(HttpStatus.OK, "배너 활성화(관리자) 성공", id);

        return new ResponseEntity<>(resDto, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "배너 수정 요청(관리자)")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editBanner(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @PathVariable Long id,
            @RequestPart BannerRequestDto bannerRequestDto,
            @RequestPart MultipartFile bannerImagePath) throws IOException {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        Banners banners = bannerService.updateBanner(id, bannerRequestDto, bannerImagePath);

        CommonResDto<Long> resDto = new CommonResDto<>(HttpStatus.OK, "배너 수정 성공(관리자)", banners.getId());

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "배너 삭제(관리자)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBanner(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @PathVariable Long id) {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        bannerService.deleteBanner(id);

        CommonResDto<Object> resDto = new CommonResDto<>(HttpStatus.OK, "배너 삭제 성공", null);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
