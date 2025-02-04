package com.buysellgo.orderservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.buysellgo.orderservice.common.dto.CommonResDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.Map;




@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {


    @PostMapping("/add")
    public ResponseEntity<CommonResDto<Map<String, Object>>> addCartItem() {
        // 장바구니 아이템 추가 로직
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "장바구니 아이템 추가 완료", null));
    }

    @GetMapping("/list")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getCartList() {
        // 장바구니 목록 조회 로직
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "장바구니 목록 조회 완료", null));
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateCartItem() {
        // 장바구니 아이템 수량 업데이트 로직
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "장바구니 아이템 수량 업데이트 완료", null));
    }

    @DeleteMapping("/delete")

    public ResponseEntity<CommonResDto<Map<String, Object>>> deleteCartItem() {
        // 장바구니 아이템 삭제 로직
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "장바구니 아이템 삭제 완료", null));
    }
}
