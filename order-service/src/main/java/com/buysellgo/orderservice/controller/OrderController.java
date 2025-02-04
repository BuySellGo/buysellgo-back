package com.buysellgo.orderservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.buysellgo.orderservice.common.dto.CommonResDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    @PostMapping("/create")
    public ResponseEntity<CommonResDto<Map<String, Object>>> createOrder() {
        // 주문 생성 로직
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "주문 생성 완료", null));
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getOrderList() {
        // 주문 목록 조회 로직
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "주문 목록 조회 완료", null));
    }

    @PutMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateOrderStatus() {
        // 주문 상태 업데이트 로직
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "주문 상태 업데이트 완료", null));
    }


}
