package com.buysellgo.userservice.controller;

import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.common.exception.CustomException;
import com.buysellgo.userservice.controller.dto.UserCreateReq;
import com.buysellgo.userservice.controller.dto.SellerCreateReq;
import com.buysellgo.userservice.strategy.sign.common.SignContext;
import com.buysellgo.userservice.strategy.sign.dto.UserSignUpDto;
import com.buysellgo.userservice.strategy.sign.dto.SellerSignUpDto;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.common.SignStrategy;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.buysellgo.userservice.common.util.CommonConstant.BEARER_PREFIX;

@Slf4j
@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
public class SignController {
    private final SignContext signContext;

    @Operation(summary = "회원가입 요청(회원)")
    @PostMapping("/user")
    public ResponseEntity<CommonResDto> userSign(@Valid @RequestBody UserCreateReq req) {
        UserSignUpDto signUpDto = UserSignUpDto.from(req);
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(Role.USER);
        SignResult<Map<String, Object>> result = strategy.signUp(signUpDto);

        if(!result.success()) {
            //에러처리
            throw new CustomException(result.message());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new CommonResDto(HttpStatus.CREATED, "회원가입 성공(회원)", result.data()));
    }

    @Operation(summary = "회원가입 요청(판매자)")
    @PostMapping("/seller")
    public ResponseEntity<CommonResDto> sellerSign(@Valid @RequestBody SellerCreateReq req) {
        SellerSignUpDto signUpDto = SellerSignUpDto.from(req);
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(Role.SELLER);
        SignResult<Map<String, Object>> result = strategy.signUp(signUpDto);

        if(!result.success()) {
           throw new CustomException(result.message());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new CommonResDto(HttpStatus.CREATED, "회원가입 성공(판매자)", result.data()));
    }

    @Operation(summary = "회원탈퇴 요청(회원)")
    @DeleteMapping("/user")
    public ResponseEntity<CommonResDto> userDelete(
            @RequestHeader(value = "Authorization", required = true) String accessToken) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new IllegalArgumentException("잘못된 Authorization 헤더 형식입니다.");
        }

        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(Role.USER);
        SignResult<Map<String, Object>> result = strategy.withdraw(token);

        if (!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResDto(HttpStatus.OK, "회원 탈퇴 완료(회원)", result.data()));
    }

    @Operation(summary = "회원탈퇴 요청(판매자)")
    @DeleteMapping("/seller")
    public ResponseEntity<CommonResDto> sellerDelete(
            @RequestHeader(value = "Authorization", required = true) String accessToken){
        if(!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new IllegalArgumentException("잘못된 Authorization 헤더 형식입니다.");
        }

        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(Role.SELLER);
        SignResult<Map<String, Object>> result = strategy.withdraw(token);

        if(!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResDto(HttpStatus.OK,"회원 탈퇴 완료(판매자)", result.data()));
    }
}
