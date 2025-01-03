package com.buysellgo.userservice.user.controller;

import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.user.controller.dto.UserCreateReq;
import com.buysellgo.userservice.user.domain.user.User;
import com.buysellgo.userservice.user.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @Operation(summary = "회원가입 요청")
    @PostMapping("/user")
    public ResponseEntity<CommonResDto> userSign(@Valid @RequestBody UserCreateReq req) {
        User.Vo vo = signService.userSign(req);
        CommonResDto dto = new CommonResDto(HttpStatus.CREATED, "회원가입 성공", vo);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

}
