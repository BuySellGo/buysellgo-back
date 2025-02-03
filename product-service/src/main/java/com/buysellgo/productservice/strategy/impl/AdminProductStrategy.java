package com.buysellgo.productservice.strategy.impl;

import org.springframework.stereotype.Component;
import com.buysellgo.productservice.strategy.common.ProductStrategy;
import com.buysellgo.productservice.common.entity.Role;
import com.buysellgo.productservice.controller.dto.ProductReq;
import com.buysellgo.productservice.strategy.common.ProductResult;
import java.util.Map;
import java.util.HashMap;
import static com.buysellgo.productservice.common.util.CommonConstant.*;
@Component
public class AdminProductStrategy implements ProductStrategy<Map<String, Object>> {


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
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.ADMIN;
    }

}
