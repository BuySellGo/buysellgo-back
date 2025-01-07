package com.buysellgo.userservice.controller;
import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.service.AuthService;
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
        Map<String, Object> jwt = authService.createJwt(req);
        if(jwt.containsKey(FAILURE.getValue())) {
            throw new NoSuchElementException(USER_NOT_FOUND.getValue());
        }

        // 응답 헤더에 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX.getValue() + jwt.get("accessToken"));
        
        // 응답 바디에서는 토큰 제외
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(EMAIL.getValue(), req.email());
        responseBody.put(ROLE.getValue(), req.role());
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
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");

        // 토큰 갱신 요청
        Map<String, Object> refreshJwt = authService.updateJwt(token);
        
        if(refreshJwt.containsKey(FAILURE.getValue())) {
            throw new NoSuchElementException(TOKEN_OR_USER_NOT_FOUND.getValue());
        }

        // 응답 헤더에 새로운 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX.getValue() + refreshJwt.get("accessToken"));
        
        // 응답 바디에서는 토큰 제외
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put(EMAIL.getValue(), refreshJwt.get(EMAIL.getValue()));
        responseBody.put(ROLE.getValue(), refreshJwt.get(ROLE.getValue()));
        responseBody.putAll(refreshJwt);
        
        CommonResDto dto = new CommonResDto(HttpStatus.CREATED, "토큰 갱신 성공", responseBody);
        return ResponseEntity.status(HttpStatus.CREATED)
                            .headers(headers)
                            .body(dto);
    }

    @Operation(summary = "로그아웃 요청(공통)")
    @DeleteMapping("/jwt")
    public ResponseEntity<CommonResDto> deleteToken(
            @RequestHeader(value = "Authorization", required = true) String accessToken
    ){
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        Map<String, Object> map = authService.deleteToken(token);
        if(map.containsKey(FAILURE.getValue())){
            throw new NoSuchElementException(TOKEN_OR_USER_NOT_FOUND.getValue());
        }
        CommonResDto dto = new CommonResDto(HttpStatus.OK,"리프레시 토큰 삭제 완료",map);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
