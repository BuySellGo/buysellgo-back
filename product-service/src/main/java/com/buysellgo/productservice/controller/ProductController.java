package com.buysellgo.productservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import com.buysellgo.productservice.common.dto.CommonResDto;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {


    @Operation(summary ="상품 생성")
    @PostMapping("/create") 
    public ResponseEntity<CommonResDto<Map<String, Object>>> createProduct(){
        //상품 생성은 판매자만 가능
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 생성 완료", null));
    }


    @Operation(summary ="상품 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getProduct(){
        // 상품 조회는 역할에 상관없이 가능
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 조회 완료", null));
    }

    @Operation(summary ="상품 상세 조회")
    @GetMapping("/detail")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getProductDetail(){
        // 상품 상세 조회는 역할에 상관없이 가능
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 상세 조회 완료", null));
    }


    @Operation(summary ="상품 수정")
    @PutMapping("/update")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateProduct(){
        //상품 수정은 판매자만 가능
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 수정 완료", null));
    }


    @Operation(summary ="상품 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<CommonResDto<Map<String, Object>>> deleteProduct(){
        //상품 삭제는 판매자와 관리자만 가능
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 삭제 완료", null));
    }


}
