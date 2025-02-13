package com.buysellgo.paymentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.buysellgo.paymentservice.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import com.buysellgo.paymentservice.common.auth.TokenUserInfo;
import com.buysellgo.paymentservice.common.auth.JwtTokenProvider;

import com.buysellgo.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import com.buysellgo.paymentservice.service.dto.ServiceResult;
import org.springframework.http.HttpStatus;
import com.buysellgo.paymentservice.common.exception.CustomException;
import com.buysellgo.paymentservice.controller.dto.PayMethodReq;
import com.buysellgo.paymentservice.controller.dto.PaymentReq;
import org.springframework.security.access.prepost.PreAuthorize;
import com.buysellgo.paymentservice.controller.dto.PaymentStatusReq;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j


public class PaymentController {
    private final PaymentService paymentService;
    private final JwtTokenProvider jwtTokenProvider;
    @Operation(summary = "결제 수단 등록")
    @PostMapping("/register")
    public ResponseEntity<CommonResDto<Map<String, Object>>> registerPayMethod(@Valid @RequestBody PayMethodReq req, @RequestHeader("Authorization") String token){
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = paymentService.registerPayMethod(req, tokenUserInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "결제 수단 등록 성공", result.data()));
    }

    @Operation(summary = "결제 수단 조회")
    @GetMapping("/list")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getPayMethodList(@RequestHeader("Authorization") String token){
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = paymentService.getPayMethodList(tokenUserInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "결제 수단 조회 성공", result.data()));
    }

    @Operation(summary = "결제 수단 수정")
    @PutMapping("/update")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updatePayMethod(@Valid @RequestBody PayMethodReq req, @RequestHeader("Authorization") String token, @RequestParam long payMethodId){
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = paymentService.updatePayMethod(req, tokenUserInfo.getId(), payMethodId);
        if(!result.success()){

            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "결제 수단 수정 성공", result.data()));
    }

    @Operation(summary = "결제 수단 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<CommonResDto<Map<String, Object>>> deletePayMethod(@RequestParam long payMethodId, @RequestHeader("Authorization") String token){
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = paymentService.deletePayMethod(payMethodId, tokenUserInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "결제 수단 삭제 성공", result.data()));
    }

    @Operation(summary = "결제 생성")
    @PostMapping("/create")
    public ResponseEntity<CommonResDto<Map<String, Object>>> createPayment(@Valid @RequestBody PaymentReq req, @RequestHeader("Authorization") String token){
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = paymentService.createPayment(req, tokenUserInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "결제 생성 성공", result.data()));
    }

    @Operation(summary = "결제 조회")
    @GetMapping("/history")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getPaymentHistory(@RequestHeader("Authorization") String token){
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = paymentService.getPaymentHistory(tokenUserInfo.getId(),tokenUserInfo.getRole());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "결제 조회 성공", result.data()));
    }

    @Operation(summary = "결제 상태 변경(관리자)")
    @PutMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updatePaymentStatus(@Valid @RequestBody PaymentStatusReq req){
        ServiceResult<Map<String, Object>> result = paymentService.updatePaymentStatus(req);
        if(!result.success()){
            throw new CustomException(result.message());

        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "결제 상태 변경 성공", result.data()));
    }







}
