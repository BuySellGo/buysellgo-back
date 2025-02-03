package com.buysellgo.productservice.strategy.impl;

import org.springframework.stereotype.Component;
import com.buysellgo.productservice.strategy.common.ProductStrategy;
import com.buysellgo.productservice.common.entity.Role;
import com.buysellgo.productservice.controller.dto.ProductReq;
import com.buysellgo.productservice.strategy.common.ProductResult;
import java.util.Map;
import java.util.HashMap;
import static com.buysellgo.productservice.common.util.CommonConstant.*;
import com.buysellgo.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.buysellgo.productservice.entity.Product;
import java.util.Optional;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional


public class AdminProductStrategy implements ProductStrategy<Map<String, Object>> {


    private final ProductRepository productRepository;



    @Override
    public ProductResult<Map<String, Object>> createProduct(ProductReq req, long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put(PRODUCT_VO.getValue(), "관리자는 상품 생성을 할 수 없습니다.");
        return ProductResult.fail(NOT_SUPPORTED.getValue(), data);
    }

    @Override
    public ProductResult<Map<String, Object>> updateProduct(ProductReq req, long userId, long productId) {
        Map<String, Object> data = new HashMap<>();
        data.put(PRODUCT_VO.getValue(), "관리자는 상품 수정을 할 수 없습니다.");
        return ProductResult.fail(NOT_SUPPORTED.getValue(), data);
    }


    @Override
    public ProductResult<Map<String, Object>> deleteProduct(long productId, long userId) {
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Product> product = productRepository.findById(productId);
            if(product.isEmpty()){
                return ProductResult.fail(PRODUCT_NOT_FOUND.getValue(), data);
            }
            data.put(PRODUCT_VO.getValue(), product.get().toVo());
            productRepository.deleteById(productId);
            return ProductResult.success(PRODUCT_DELETE_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(PRODUCT_VO.getValue(), e.getMessage());
            return ProductResult.fail(PRODUCT_DELETE_FAIL.getValue(), data);
        }

    }

    @Override
    public ProductResult<Map<String, Object>> getProductList(long userId) {
        Map<String, Object> data = new HashMap<>();
        try{
            List<Product> products = productRepository.findAll();
            if(products.isEmpty()){
                return ProductResult.fail(PRODUCT_LIST_EMPTY.getValue(), data);
            }
            data.put(PRODUCT_VO.getValue(), products.stream().map(Product::toVo).toList());
            return ProductResult.success(PRODUCT_LIST_SUCCESS.getValue(), data);

        } catch (Exception e) {
            data.put(PRODUCT_VO.getValue(), e.getMessage());
            return ProductResult.fail(PRODUCT_LIST_FAIL.getValue(), data);
        }
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.ADMIN;
    }


}
