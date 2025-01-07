package com.buysellgo.userservice.controller;
import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.service.AuthService;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.HashMap;

import static com.buysellgo.userservice.common.util.CommonConstant.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인 요청(공통)")
    @PostMapping("/jwt")
    public ResponseEntity<CommonResDto> createJwt(@Valid @RequestBody JwtCreateReq req) {
        AuthResult<?> result = authService.createJwt(req);
        if (!result.success()) {
            throw new NoSuchElementException(USER_NOT_FOUND.getValue());
        }

        Map<String, Object> tokenInfo = (Map<String, Object>) result.data();
        
        // 응답 헤더에 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX.getValue() + tokenInfo.get(ACCESS_TOKEN.getValue()));
        
        // 응답 바디에서는 토큰 제외
        Map<String, Object> responseBody = new HashMap<>(tokenInfo);
        responseBody.remove(ACCESS_TOKEN.getValue());
        
        CommonResDto dto = new CommonResDto(HttpStatus.CREATED, "로그인 성공", responseBody);
        return ResponseEntity.status(HttpStatus.CREATED)
                        .headers(headers)
                        .body(dto);
    }

    @Operation(summary = "토큰 갱신(공통)")
    @PutMapping("/jwt")
    public ResponseEntity<CommonResDto> refreshToken(
            @RequestHeader(value = "Authorization", required = false) String accessToken) {
        // Authorization 헤더가 없는 경우
        if (accessToken == null) {
            throw new IllegalArgumentException("리프레시 토큰이 필요합니다.");
        }

        // Bearer 접두어 검증
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new IllegalArgumentException("잘못된 Authorization 헤더 형식입니다.");
        }

        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        AuthResult<?> result = authService.updateJwt(token);
        
        if (!result.success()) {
            throw new NoSuchElementException("Refresh token not found");
        }

        Map<String, Object> tokenInfo = (Map<String, Object>) result.data();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX.getValue() + tokenInfo.get("accessToken"));
        
        Map<String, Object> responseBody = new HashMap<>(tokenInfo);
        responseBody.remove("accessToken");
        
        CommonResDto dto = new CommonResDto(HttpStatus.OK, "토큰 갱신 성공", responseBody);
        return ResponseEntity.ok()
                        .headers(headers)
                        .body(dto);
    }

    @Operation(summary = "로그아웃 요청(공통)")
    @DeleteMapping("/jwt")
    public ResponseEntity<CommonResDto> deleteToken(
            @RequestHeader(value = "Authorization") String accessToken) {
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        AuthResult<?> result = authService.deleteToken(token);
        
        if (!result.success()) {
            throw new NoSuchElementException(TOKEN_OR_USER_NOT_FOUND.getValue());
        }
        
        CommonResDto dto = new CommonResDto(HttpStatus.OK, "로그아웃 성공", result.data());
        return ResponseEntity.ok(dto);
    }
}
