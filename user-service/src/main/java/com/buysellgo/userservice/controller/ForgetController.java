package com.buysellgo.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.common.exception.CustomException;
import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.strategy.forget.common.ForgetStrategy;

import lombok.RequiredArgsConstructor;

import com.buysellgo.userservice.strategy.forget.common.ForgetResult;
import com.buysellgo.userservice.strategy.forget.common.ForgetContext;

import io.swagger.v3.oas.annotations.Operation;

import java.util.Map;

@RestController
@RequestMapping("/forget")
@RequiredArgsConstructor
public class ForgetController {
    private final ForgetContext forgetContext;

    @Operation(summary = "이메일 찾기(이메일 검증)")
    @GetMapping("/email")
    public ResponseEntity<CommonResDto> forgetEmail(@RequestHeader("X-Email") String email, @RequestHeader("X-Role") Role role) {
        ForgetStrategy<Map<String,Object>> strategy = forgetContext.getStrategy(role);
        ForgetResult<Map<String,Object>> result = strategy.forgetEmail(email);

        if (!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, result.message(), result.data()));
    }

    @Operation(summary = "비밀번호 찾기(임시 비밀번호 발급)")
    @PostMapping("/password")
    public ResponseEntity<CommonResDto> forgetPassword(@RequestHeader("X-Email") String email, @RequestHeader("X-Role") Role role) {
        ForgetStrategy<Map<String,Object>> strategy = forgetContext.getStrategy(role);
        ForgetResult<Map<String,Object>> result = strategy.forgetPassword(email);

        if (!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, result.message(), result.data()));
    }
}

