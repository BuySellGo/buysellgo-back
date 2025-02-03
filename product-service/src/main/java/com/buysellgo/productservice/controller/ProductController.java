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
import com.buysellgo.productservice.strategy.common.ProductContext;
import com.buysellgo.productservice.strategy.common.ProductStrategy;
import com.buysellgo.productservice.strategy.common.ProductResult;
import com.buysellgo.productservice.common.auth.JwtTokenProvider;
import com.buysellgo.productservice.controller.dto.ProductReq;
import com.buysellgo.productservice.common.auth.TokenUserInfo;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import com.buysellgo.productservice.common.exception.CustomException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;





@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {    
    private final ProductContext productContext;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary ="상품 생성")
    @PostMapping("/create") 
    public ResponseEntity<CommonResDto<Map<String, Object>>> createProduct(@RequestHeader("Authorization") String token, @Valid @RequestBody ProductReq req){
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ProductStrategy<Map<String, Object>> strategy = productContext.getStrategy(tokenUserInfo.getRole());
        ProductResult<Map<String, Object>> result = strategy.createProduct(req, tokenUserInfo.getId());
        if(!result.success()){

            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 생성 완료", result.data()));
    }


    @Operation(summary ="상품 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getProduct(@RequestHeader("Authorization") String token){
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ProductStrategy<Map<String, Object>> strategy = productContext.getStrategy(tokenUserInfo.getRole());
        ProductResult<Map<String, Object>> result = strategy.getProductList(tokenUserInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }

        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 조회 완료", result.data()));
    }


    @Operation(summary ="상품 상세 조회")
    @GetMapping("/detail")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getProductDetail(){
        // 상품 상세 조회는 역할에 상관없이 가능
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 상세 조회 완료", null));
    }


    @Operation(summary ="상품 수정")
    @PutMapping("/update")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateProduct(@RequestHeader("Authorization") String token,@RequestParam("productId") long productId,@Valid @RequestBody ProductReq req){
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ProductStrategy<Map<String, Object>> strategy = productContext.getStrategy(tokenUserInfo.getRole());
        ProductResult<Map<String, Object>> result = strategy.updateProduct(req, tokenUserInfo.getId(), productId);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 수정 완료", result.data()));
    }



    @Operation(summary ="상품 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<CommonResDto<Map<String, Object>>> deleteProduct(@RequestHeader("Authorization") String token, @RequestParam("productId") long productId){
        TokenUserInfo tokenUserInfo = jwtTokenProvider.getTokenUserInfo(token);
        ProductStrategy<Map<String, Object>> strategy = productContext.getStrategy(tokenUserInfo.getRole());
        ProductResult<Map<String, Object>> result = strategy.deleteProduct(productId, tokenUserInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 삭제 완료", result.data()));
    }


}
