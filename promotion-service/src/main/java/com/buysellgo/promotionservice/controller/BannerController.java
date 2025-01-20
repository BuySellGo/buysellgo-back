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
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/create")
    public ResponseEntity<?> createBanner(
//            @Valid @RequestBody BannerRequestDto bannerRequestDto) throws IOException {
            @RequestPart("bannerRequestDto") BannerRequestDto bannerRequestDto,
            @RequestPart("bannerImagePath") MultipartFile bannerImagePath) throws IOException {

        // 날짜 값 처리
        System.out.println("배너 제목: " + bannerRequestDto.getBannerTitle());
//        System.out.println("배너 이미지 파일 이름: " + bannerRequestDto.getBannerImagePath().getOriginalFilename());
        System.out.println("배너 URL: " + bannerRequestDto.getBannerUrl());
        System.out.println("시작 시간: " + bannerRequestDto.getStartDate());
        System.out.println("종료 시간: " + bannerRequestDto.getEndDate());

//        bannerRequestDto.setBannerImagePath(bannerImagePath);


        log.info("createBanner: {}", bannerRequestDto);
        Banners banner = bannerService.createBanner(bannerRequestDto, bannerImagePath);

        // 배너 생성 실패 시 처리
        if (banner == null) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR,"배너 생성 실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "배너 생성 성공", banner.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }

}
