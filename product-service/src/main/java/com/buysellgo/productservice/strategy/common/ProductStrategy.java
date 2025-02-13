package com.buysellgo.productservice.strategy.common;

import java.util.Map;
import com.buysellgo.productservice.common.entity.Role;
import com.buysellgo.productservice.controller.dto.ProductReq;
    
public interface ProductStrategy<T extends Map<String, Object>> {
    /**
     * 상품을 생성합니다.
     *
     * @param req 상품 생성 요청 정보를 포함하는 ProductReq입니다.
     * @param userId 생성할 사용자의 ID입니다.
     * @return 상품 생성 결과를 포함하는 ProductResult입니다.
     */
    ProductResult<T> createProduct(ProductReq req, long userId);

    /**
     * 상품을 수정합니다.
     *
     * @param req 상품 수정 요청 정보를 포함하는 ProductReq입니다.
     * @param userId 수정할 사용자의 ID입니다.
     * @param productId 수정할 상품의 ID입니다.
     * @return 상품 수정 결과를 포함하는 ProductResult입니다.
     */
    ProductResult<T> updateProduct(ProductReq req, long userId, long productId);

    /**
     * 상품을 삭제합니다.
     *
     * @param productId 삭제할 상품의 ID입니다.
     * @param userId 삭제할 사용자의 ID입니다.
     * @return 상품 삭제 결과를 포함하는 ProductResult입니다.
     */
    ProductResult<T> deleteProduct(long productId, long userId);

    /**
     * 상품 목록을 조회합니다.
     *
     * @return 상품 목록을 포함하는 ProductResult입니다.
     */
    ProductResult<T> getProductList(long userId);

    /**
     * 주어진 역할을 이 전략이 지원하는지 여부를 결정합니다.

     *
     * @param role 확인할 역할입니다.
     * @return 지원 여부를 나타내는 boolean 값입니다.
     */
    boolean supports(Role role);
}
