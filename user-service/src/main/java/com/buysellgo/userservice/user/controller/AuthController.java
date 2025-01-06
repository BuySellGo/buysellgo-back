package com.buysellgo.userservice.user.controller;
import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.user.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.user.service.AuthService;
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

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private static final String USER_NOT_FOUND_MESSAGE = "사용자를 찾을 수 없습니다.";
    private static final String TOKEN_OR_USER_NOT_FOUND_MESSAGE = "리프레시 토큰이 만료되었거나, 해당 사용자가 존재하지 않습니다.";
    private static final String EMAIL = "email";
    private static final String ROLE= "role";
    private static final String HEADVALUE = "Bearer ";

    @Operation(summary = "로그인 요청(공통)")
    @PostMapping("/jwt")
    public ResponseEntity<CommonResDto> createJwt(@Valid @RequestBody JwtCreateReq req) {
        Map<String, Object> jwt = authService.createJwt(req);
        if(jwt.containsKey("failure")) { 
            throw new NoSuchElementException(USER_NOT_FOUND_MESSAGE); 
        }

        // 응답 헤더에 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, HEADVALUE + jwt.get("accessToken"));
        
        // 응답 바디에서는 토큰 제외
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(EMAIL, req.email());
        responseBody.put(ROLE, req.role());
        responseBody.putAll(jwt);
        
        CommonResDto dto = new CommonResDto(HttpStatus.CREATED, "로그인 성공", responseBody);
        return ResponseEntity.status(HttpStatus.CREATED)
                            .headers(headers)
                            .body(dto);
    }

    @Operation(summary = "토큰 갱신(공통)")
    @PutMapping("/jwt")
    public ResponseEntity<CommonResDto> refreshToken(
            @RequestHeader(value = "Authorization", required = true) String accessToken) {
        
        // Bearer 제거 및 토큰 검증
        String token = accessToken.replace(HEADVALUE, "");

        // 토큰 갱신 요청
        Map<String, Object> refreshJwt = authService.updateJwt(token);
        
        if(refreshJwt.containsKey("failure")) {
            throw new NoSuchElementException(TOKEN_OR_USER_NOT_FOUND_MESSAGE);
        }

        // 응답 헤더에 새로운 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, HEADVALUE + refreshJwt.get("accessToken"));
        
        // 응답 바디에서는 토큰 제외
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(EMAIL, refreshJwt.get(EMAIL));
        responseBody.put(ROLE, refreshJwt.get(ROLE));
        responseBody.putAll(refreshJwt);
        
        CommonResDto dto = new CommonResDto(HttpStatus.CREATED, "토큰 갱신 성공", responseBody);
        return ResponseEntity.status(HttpStatus.CREATED)
                            .headers(headers)
                            .body(dto);
    }
}
