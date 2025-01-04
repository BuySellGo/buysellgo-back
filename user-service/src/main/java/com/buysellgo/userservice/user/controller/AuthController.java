package com.buysellgo.userservice.user.controller;

import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.user.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private static final String USER_NOT_FOUND_MESSAGE = "사용자를 찾을 수 없습니다.";

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
}
