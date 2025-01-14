package com.buysellgo.userservice.controller;
import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.common.exception.CustomException;
import com.buysellgo.userservice.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.strategy.auth.common.AuthContext;
import com.buysellgo.userservice.strategy.auth.common.AuthStrategy;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
import com.buysellgo.userservice.strategy.auth.dto.AuthDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import static com.buysellgo.userservice.common.util.CommonConstant.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthContext authContext;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "로그인 요청(공통)")
    @PostMapping("/jwt")
    public ResponseEntity<CommonResDto> createJwt(@Valid @RequestBody JwtCreateReq req) {

        AuthStrategy<Map<String, Object>> strategy = authContext.getStrategy(req.role());
        AuthResult<Map<String, Object>> result = strategy.createJwt(AuthDto.from(req));

        if(!result.success()){
            throw new CustomException(result.message());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX.getValue() + result.message());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                        .headers(headers)
                        .body(new CommonResDto(HttpStatus.CREATED, "로그인 성공", result.data()));
    }

    @Operation(summary = "토큰 갱신(공통)")
    @PutMapping("/jwt")
    public ResponseEntity<CommonResDto> refreshToken(
            @RequestHeader(value = "Authorization", required = false) String accessToken) {
        if (accessToken == null) {
            throw new IllegalArgumentException("리프레시 토큰이 필요합니다.");
        }

        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new IllegalArgumentException("잘못된 Authorization 헤더 형식입니다.");
        }

        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        AuthStrategy<Map<String, Object>> strategy = authContext.getStrategy(userInfo.getRole());
        AuthResult<Map<String, Object>> result = strategy.updateJwt(token);

        if(!result.success()){
            throw new CustomException(result.message());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX.getValue() + result.message());
        
        return ResponseEntity.ok()
                        .headers(headers)
                        .body(new CommonResDto(HttpStatus.OK, "토큰 갱신 성공", result.data()));
    }

    @Operation(summary = "로그아웃 요청(공통)")
    @DeleteMapping("/jwt")
    public ResponseEntity<CommonResDto> deleteToken(
            @RequestHeader(value = "Authorization") String accessToken) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new IllegalArgumentException("잘못된 Authorization 헤더 형식입니다.");
        }

        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        AuthStrategy<Map<String, Object>> strategy = authContext.getStrategy(userInfo.getRole());
        AuthResult<Map<String, Object>> result = strategy.deleteToken(token);

        if(!result.success()){
            throw new CustomException(result.message());
        }
        
        return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "로그아웃 성공", result.data()));
    }
}
