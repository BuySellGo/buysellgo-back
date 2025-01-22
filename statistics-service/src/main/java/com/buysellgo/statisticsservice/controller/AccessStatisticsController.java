package com.buysellgo.statisticsservice.controller;

import com.buysellgo.statisticsservice.common.auth.TokenUserInfo;
import com.buysellgo.statisticsservice.common.dto.CommonResDto;
import com.buysellgo.statisticsservice.entity.AccessStatistics;
import com.buysellgo.statisticsservice.service.AccessStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/access-stats")
@RequiredArgsConstructor
@Slf4j
public class AccessStatisticsController {
    private final AccessStatisticsService accessStatisticsService;

    @Operation(summary = "회원 접속 통계")
    @PostMapping("/record")
    public ResponseEntity<?> recordAccessStatistics(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            HttpServletRequest request) {

        // 인증된 사용자 정보(sellerId) 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        String accessIp = getClientIp(request);

        AccessStatistics accessStatistics
                = accessStatisticsService.addAccessRecord(tokenUserInfo.getId(), accessIp);

        if (accessStatistics == null) {
            return new ResponseEntity<>(
                    new CommonResDto<>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "접속 기록 실패",
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto<Long> resDto
                = new CommonResDto<>(HttpStatus.CREATED, "접속 기록 성공", accessStatistics.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }

        // X-Forwarded-For 헤더에는 여러 개의 IP가 있을 수 있으므로, 첫 번째 IP를 사용
        if (ipAddress != null && ipAddress.indexOf(',') > 0) {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        // IPv6가 포함되어 있을 경우 IPv4 형식으로 필터링
        if (ipAddress != null && ipAddress.contains(":")) {
            // 0:0:0:0:0:0:0:1 (IPv6 loopback 주소 처리)
            if (ipAddress.equals("0:0:0:0:0:0:0:1") || ipAddress.equals("::1")) {
                ipAddress = "127.0.0.1"; // 로컬 IP로 변환
            } else {
                ipAddress = extractIpv4FromIpv6(ipAddress); // IPv6에서 IPv4로 변환
            }
        }

        log.info("getClientIp: {}", ipAddress);

        return ipAddress;
    }

    // IPv6 주소에서 IPv4 주소 추출 (IPv6를 포함한 IPv4 주소인 경우)
    private String extractIpv4FromIpv6(String ipAddress) {
        if (ipAddress.startsWith("::ffff:")) {
            // "::ffff:" 접두사를 포함한 IPv6를 IPv4로 변환
            return ipAddress.substring(7); // "::ffff:" 부분을 잘라냄
        }
        return ipAddress; // IPv4 주소라면 그대로 반환
    }

    @Operation(summary = "월간 접속 통계 현황")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monthly")
    public ResponseEntity<?> monthlyAccessStatistics(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestParam int year, @RequestParam int month) {

        accessStatisticsService.getMonthlyAccessStats(year, month)
    }

}
