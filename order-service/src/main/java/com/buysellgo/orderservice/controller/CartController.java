package com.buysellgo.orderservice.controller;

import com.buysellgo.orderservice.controller.dto.CartOrderReq;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;   
import com.buysellgo.orderservice.service.CartService;
import com.buysellgo.orderservice.common.auth.JwtTokenProvider;
import com.buysellgo.orderservice.common.auth.TokenUserInfo;
import com.buysellgo.orderservice.service.dto.ServiceResult;
import com.buysellgo.orderservice.common.exception.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import com.buysellgo.orderservice.controller.dto.CartCreateReq;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor    
@Slf4j
public class CartController {
    private final CartService cartService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "장바구니 아이템 추가")
    @PostMapping("/add")
    public ResponseEntity<CommonResDto<Map<String, Object>>> addCartItem(@Valid @RequestBody CartCreateReq req, @RequestHeader(name = "Authorization") String token) {
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = cartService.addCartItem(req, tokenUserInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "장바구니 아이템 추가 완료", result.data()));

    }

    @Operation(summary = "장바구니 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getCartList(@RequestHeader(name = "Authorization") String token) {
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = cartService.getCartList(tokenUserInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "장바구니 목록 조회 완료", result.data()));
    }

    @Operation(summary = "장바구니 아이템 수량 업데이트")
    @PutMapping("/update")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateCartItem(@RequestParam long cartId, @RequestParam int quantity,@RequestHeader(name = "Authorization") String token) {
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = cartService.updateCartItem(cartId, quantity, tokenUserInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "장바구니 아이템 수량 업데이트 완료", result.data()));
    }

    @Operation(summary = "장바구니 아이템 주문")
    @PostMapping("/order")
    public ResponseEntity<CommonResDto<Map<String, Object>>> orderCartItem(@Valid @RequestBody CartOrderReq req, @RequestHeader(name = "Authorization") String token) {
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = cartService.orderCartItem(req, tokenUserInfo.getId());
        if(!result.success()){

            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "장바구니 아이템 주문 완료", result.data()));
    }


    @Operation(summary = "장바구니 아이템 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<CommonResDto<Map<String, Object>>> deleteCartItem(@RequestParam long cartId, @RequestHeader(name = "Authorization") String token) {
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ServiceResult<Map<String, Object>> result = cartService.deleteCartItem(cartId, tokenUserInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "장바구니 아이템 삭제 완료", result.data()));
    }

}
