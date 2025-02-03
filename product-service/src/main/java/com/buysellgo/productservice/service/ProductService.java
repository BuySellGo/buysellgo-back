package com.buysellgo.productservice.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;    
import lombok.extern.slf4j.Slf4j;
import com.buysellgo.productservice.repository.ProductRepository;
import com.buysellgo.productservice.entity.Product;
import java.util.Optional;
import static com.buysellgo.productservice.common.util.CommonConstant.*;
import com.buysellgo.productservice.service.dto.ServiceResult;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ServiceResult<Map<String, Object>> getProductDetail(long productId) {
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Product> product = productRepository.findById(productId);
            if(product.isEmpty()){
                return ServiceResult.fail(PRODUCT_NOT_FOUND.getValue(), data);
            }

            data.put(PRODUCT_VO.getValue(), product.get().toVo());
            return ServiceResult.success(PRODUCT_DETAIL_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(PRODUCT_VO.getValue(), e.getMessage());
            return ServiceResult.fail(PRODUCT_DETAIL_FAIL.getValue(), data);
        }
    }

    public ServiceResult<Map<String, Object>> getProductList() {
        Map<String, Object> data = new HashMap<>();
        try{
            List<Product> products = productRepository.findAll();
            if(products.isEmpty()){
                return ServiceResult.fail(PRODUCT_LIST_EMPTY.getValue(), data);
            }
            data.put(PRODUCT_VO.getValue(), products.stream().map(Product::toVo).toList());
            return ServiceResult.success(PRODUCT_LIST_SUCCESS.getValue(), data);

        } catch (Exception e) {
            data.put(PRODUCT_VO.getValue(), e.getMessage());
            return ServiceResult.fail(PRODUCT_LIST_FAIL.getValue(), data);
        }
    }   
}
