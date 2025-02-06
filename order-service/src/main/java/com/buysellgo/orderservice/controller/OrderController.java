package com.buysellgo.orderservice.controller;

import com.buysellgo.orderservice.controller.dto.OrderCreateReq;
import com.buysellgo.orderservice.controller.dto.OrderStatusUpdateReq;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.buysellgo.orderservice.common.dto.CommonResDto;

import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import com.buysellgo.orderservice.service.OrderService;
import com.buysellgo.orderservice.service.dto.ServiceResult;
import com.buysellgo.orderservice.common.auth.JwtTokenProvider;
import com.buysellgo.orderservice.common.auth.TokenUserInfo;
import com.buysellgo.orderservice.common.exception.CustomException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor

@Slf4j

public class OrderController {

    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;



    @PostMapping("/create")
    public ResponseEntity<CommonResDto<Map<String, Object>>> createOrder(@RequestHeader("Authorization") String token, @Valid @RequestBody OrderCreateReq req) {
        // 주문 생성 로직
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = orderService.createOrder(tokenUserInfo.getId(), req);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "주문 생성 완료", result.data()));

    }

    @GetMapping("/list")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getOrderList(@RequestHeader("Authorization") String token) {
        // 주문 목록 조회 로직
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = orderService.getOrderList(tokenUserInfo.getId(), tokenUserInfo.getRole());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "주문 목록 조회 완료", result.data()));
    }


    @PutMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateOrderStatus(@RequestHeader("Authorization") String token, @Valid @RequestBody OrderStatusUpdateReq req) {
        // 주문 상태 업데이트 로직
        ServiceResult<Map<String, Object>> result = orderService.updateOrderStatus(req);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "주문 상태 업데이트 완료", result.data()));

    }

    @PutMapping("/status/success")
    public ServiceResult<Map<String, Object>> updateOrderStatusSuccess(@RequestParam Long orderId){
        return orderService.updateOrderStatusSuccess(orderId);
    }

    @PutMapping("/status/cancelled")
    public ServiceResult<Map<String, Object>> updateOrderStatusCancelled(@RequestParam Long orderId){
        return orderService.updateOrderStatusCancelled(orderId);
    }


}
