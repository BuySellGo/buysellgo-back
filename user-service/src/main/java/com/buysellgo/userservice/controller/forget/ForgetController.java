package com.buysellgo.userservice.controller.forget;

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
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Map;

@RestController
@RequestMapping("/forget")
@RequiredArgsConstructor
public class ForgetController {
    private final ForgetContext forgetContext;

    @Operation(summary = "이메일 찾기(이메일 검증)", description = "사용자의 이메일을 검증합니다.")
    @GetMapping("/email")
    public ResponseEntity<CommonResDto<Object>> forgetEmail(
            @Parameter(description = "사용자의 이메일 주소", example = "user@example.com")
            @RequestHeader("X-Email") String email,
            @Parameter(description = "사용자의 역할", example = "USER")
            @RequestHeader("X-Role") Role role) {
        // 사용자의 역할에 맞는 이메일 검증 전략을 가져옴
        ForgetStrategy<Map<String,Object>> strategy = forgetContext.getStrategy(role);
        // 이메일 검증 요청을 처리
        ForgetResult<Map<String,Object>> result = strategy.forgetEmail(email);

        if (!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, result.message(), result.data()));
    }

    @Operation(summary = "비밀번호 찾기(임시 비밀번호 발급)", description = "사용자의 비밀번호를 재설정합니다.")
    @PostMapping("/password")
    public ResponseEntity<CommonResDto<Object>> forgetPassword(
            @Parameter(description = "사용자의 이메일 주소", example = "user@example.com")
            @RequestHeader("X-Email") String email,
            @Parameter(description = "사용자의 역할", example = "USER")
            @RequestHeader("X-Role") Role role) {
        // 사용자의 역할에 맞는 비밀번호 초기화 전략을 가져옴
        ForgetStrategy<Map<String,Object>> strategy = forgetContext.getStrategy(role);
        // 비밀번호 초기화 요청을 처리
        ForgetResult<Map<String,Object>> result = strategy.forgetPassword(email);

        if (!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, result.message(), result.data()));
    }
}

