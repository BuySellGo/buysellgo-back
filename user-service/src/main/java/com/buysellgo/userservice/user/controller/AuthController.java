package com.buysellgo.userservice.user.controller;

import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.user.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.user.controller.dto.JwtUpdateReq;
import com.buysellgo.userservice.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private static final String USER_NOT_FOUND_MESSAGE = "사용자를 찾을 수 없습니다.";
    private static final String TOKEN_OR_USER_NOT_FOUND_MESSAGE="리프레시 토큰이 만료되었거나, 해당 사용자가 존재하지 않습니다.";

    @Operation(summary = "로그인 요청(공통)")
    @PostMapping("/jwt")
    public ResponseEntity<CommonResDto> createJwt(@Valid @RequestBody JwtCreateReq req){
        Map<String, Object> jwt = authService.createJwt(req);
        if(jwt.containsKey("failure")){ 
            throw new NoSuchElementException(USER_NOT_FOUND_MESSAGE); 
        }
        jwt.put("email",req.email());
        jwt.put("role",req.role());
        CommonResDto dto = new CommonResDto(HttpStatus.CREATED, "로그인 성공", jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "토큰 갱신(공통)")
    @PutMapping("/jwt")
    public ResponseEntity<CommonResDto> updateJwt(@Valid @RequestBody JwtUpdateReq req){
        Map<String, Object> refreshJwt = authService.updateJwt(req);
        if(refreshJwt.containsKey("failure")){
            throw new NoSuchElementException(TOKEN_OR_USER_NOT_FOUND_MESSAGE);
        }
        refreshJwt.put("email",req.email());
        refreshJwt.put("role",req.role());
        CommonResDto dto = new CommonResDto(HttpStatus.CREATED, "토큰 갱신 성공", refreshJwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
