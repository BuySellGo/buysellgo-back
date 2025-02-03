package com.buysellgo.productservice.strategy.impl;

import org.springframework.stereotype.Component;
import com.buysellgo.productservice.strategy.common.ProductStrategy;
import com.buysellgo.productservice.common.entity.Role;
import com.buysellgo.productservice.controller.dto.ProductReq;
import com.buysellgo.productservice.strategy.common.ProductResult;
import java.util.Map;
import java.util.HashMap;
import static com.buysellgo.productservice.common.util.CommonConstant.*;
import com.buysellgo.productservice.strategy.dto.ProductDto;
import com.buysellgo.productservice.entity.Product;
import com.buysellgo.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Transactional
@Component
@RequiredArgsConstructor

public class SellerProductStrategy implements ProductStrategy<Map<String, Object>> {

    private final ProductRepository productRepository;

    
    @Override
    public ProductResult<Map<String, Object>> createProduct(ProductReq req, long userId) {
        Map<String, Object> data = new HashMap<>();
        try{
            ProductDto productDto = ProductDto.from(req, userId);
            if(productRepository.findByProductName(productDto.getProductName()).isPresent()){
                return ProductResult.fail(PRODUCT_DUPLICATED.getValue(), data);
            }
            Product product = productRepository.save(Product.of(productDto.getProductName(), productDto.getPrice(), productDto.getCompanyName(), productDto.getSellerId(), productDto.getProductImage(), productDto.getDescription(), productDto.getProductStock(), productDto.getDiscountRate(), productDto.getDeliveryFee(), productDto.getMainCategory(), productDto.getSubCategory(), productDto.getSeason()));
            data.put(PRODUCT_VO.getValue(), product.toVo());
            return ProductResult.success(PRODUCT_CREATE_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(PRODUCT_VO.getValue(), e.getMessage());
            return ProductResult.fail(PRODUCT_CREATE_FAIL.getValue(), data);
        }
    }



    @Override
    public ProductResult<Map<String, Object>> updateProduct(ProductReq req, long userId, long productId) {
        return null;
    }

    @Override
    public ProductResult<Map<String, Object>> deleteProduct(long productId, long userId) {
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.SELLER;
    }


    
}
