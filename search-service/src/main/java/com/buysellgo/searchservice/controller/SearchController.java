package com.buysellgo.searchservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buysellgo.searchservice.common.dto.CommonResDto;
import com.buysellgo.searchservice.service.SearchService;
import com.buysellgo.searchservice.service.dto.ServiceResult;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.buysellgo.searchservice.common.exception.CustomException;
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
@Slf4j



public class SearchController {
    private final SearchService searchService;

    @Operation(summary = "상품 검색")
    @GetMapping("/products")
    public ResponseEntity<CommonResDto<Map<String, Object>>> searchProducts(@RequestParam String keyword) {
        ServiceResult<Map<String, Object>> result = searchService.searchProducts(keyword);
        if (!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "상품 검색 성공", result.data()));

    }

    @Operation(summary = "메인 카테고리별 상품 조회")
    @GetMapping("/main-categories")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getMainCategories(@RequestParam String keyword) {
        ServiceResult<Map<String, Object>> result = searchService.getMainCategories(keyword);
        if (!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "메인 카테고리 조회 성공", result.data()));
    }


    @Operation(summary = "서브 카테고리별 상품 조회")
    @GetMapping("/sub-categories")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getSubCategories(@RequestParam String keyword) {
        ServiceResult<Map<String, Object>> result = searchService.getSubCategories(keyword);
        if (!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "서브 카테고리 조회 성공", result.data()));
    }
    
    @Operation(summary = "시즌별 상품 조회")
    @GetMapping("/seasons")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getSeasons(@RequestParam String keyword) {
        ServiceResult<Map<String, Object>> result = searchService.getSeasons(keyword);
        if (!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "시즌별 상품 조회 성공", result.data()));
    }
}
