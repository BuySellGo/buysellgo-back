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
import org.springframework.kafka.core.KafkaTemplate;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

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

    public ServiceResult<Map<String, Object>> updateProductQuantity(long productId, int quantity){
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Product> productOptional = productRepository.findById(productId);
            if(productOptional.isEmpty()){
                return ServiceResult.fail(PRODUCT_NOT_FOUND.getValue(), data);
            }
            Product product = productOptional.get();
            if(product.getProductStock() <= quantity){
                log.info("상품 수량 업데이트 실패: 상품 수량 부족");
                return ServiceResult.fail(PRODUCT_QUANTITY_UPDATE_FAIL.getValue(), data);
            }
            product.setProductStock(product.getProductStock() - quantity);
            productRepository.save(product);
            log.info("상품 수량 업데이트 완료: {}", product.toVo());
            data.put(PRODUCT_VO.getValue(), product.toVo());
            kafkaTemplate.send("product-update", product);
            return ServiceResult.success(PRODUCT_QUANTITY_UPDATE_SUCCESS.getValue(), data);


        } catch (Exception e) {
            data.put(PRODUCT_VO.getValue(), e.getMessage());    
            log.info("상품 수량 업데이트 실패: {}", e.getMessage());
            return ServiceResult.fail(PRODUCT_QUANTITY_UPDATE_FAIL.getValue(), data);
        }
    }

    public ServiceResult<Map<String, Object>> restoreProductQuantity(long productId, int quantity){
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Product> productOptional = productRepository.findById(productId);
            if(productOptional.isEmpty()){
                return ServiceResult.fail(PRODUCT_NOT_FOUND.getValue(), data);
            }
            Product product = productOptional.get();
            product.setProductStock(product.getProductStock() + quantity);
            productRepository.save(product);
            log.info("상품 수량 복원 완료: {}", product.toVo());
            data.put(PRODUCT_VO.getValue(), product.toVo());
            kafkaTemplate.send("product-update", product);
            return ServiceResult.success(PRODUCT_QUANTITY_RESTORE_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(PRODUCT_VO.getValue(), e.getMessage());
            log.info("상품 수량 복원 실패: {}", e.getMessage());
            return ServiceResult.fail(PRODUCT_QUANTITY_RESTORE_FAIL.getValue(), data);
        }
    }


}
