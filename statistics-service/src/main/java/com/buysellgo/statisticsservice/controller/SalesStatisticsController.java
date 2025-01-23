package com.buysellgo.statisticsservice.controller;

import com.buysellgo.statisticsservice.common.auth.TokenUserInfo;
import com.buysellgo.statisticsservice.common.dto.CommonResDto;
import com.buysellgo.statisticsservice.dto.SalesStatRequestDto;
import com.buysellgo.statisticsservice.entity.SalesStatistics;
import com.buysellgo.statisticsservice.service.SalesStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales-stats")
@RequiredArgsConstructor
@Slf4j
public class SalesStatisticsController {

    private final SalesStatisticsService salesStatisticsService;

    @Operation(summary = "매출 통계 기록")
    @PostMapping("/record")
    public ResponseEntity<?> recordSalesStatistics(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestBody SalesStatRequestDto salesStatRequestDto){

        log.info("salesStatRequestDto: {}", salesStatRequestDto);

        // 인증된 사용자 정보 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        SalesStatistics.Vo vo = salesStatisticsService.addSalesRecord(salesStatRequestDto);

        if (vo == null) {
            return new ResponseEntity<>(
                    new CommonResDto<>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "매출 통계 기록 실패",
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto<Long> resDto
                = new CommonResDto<>(HttpStatus.CREATED, "매출 통계 기록 성공", vo.id());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }

    @Operation(summary = "월간 매출 통계 현황")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @GetMapping("/monthly")
    public ResponseEntity<?> monthlySalesStatistics(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestParam @Min(2000) int year,
            @RequestParam @Min(1) @Max(12) int month){

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }
        log.info("year: {}, month: {}", year, month);

        SalesStatisticsService.MonthlySalesStatisticsReport monthlySalesStatisticsReport
                = salesStatisticsService.getMonthlySalesStatisticsReport(year, month, tokenUserInfo);

        CommonResDto<SalesStatisticsService.MonthlySalesStatisticsReport> resDto
                = new CommonResDto<>(HttpStatus.OK,
                "월간 매출 통계 현황 조회 성공", monthlySalesStatisticsReport);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
