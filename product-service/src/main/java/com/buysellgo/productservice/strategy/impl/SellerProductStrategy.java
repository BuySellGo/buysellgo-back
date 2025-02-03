package com.buysellgo.productservice.strategy.impl;

import org.springframework.stereotype.Component;
import com.buysellgo.productservice.strategy.common.ProductStrategy;
import com.buysellgo.productservice.common.entity.Role;
import com.buysellgo.productservice.controller.dto.ProductReq;
import com.buysellgo.productservice.strategy.common.ProductResult;
import java.util.Map;

@Component
public class SellerProductStrategy implements ProductStrategy<Map<String, Object>> {



    @Override
    public ProductResult<Map<String, Object>> createProduct(ProductReq req, long userId) {
        return null;
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
