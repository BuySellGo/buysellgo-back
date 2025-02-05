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
import java.util.Optional;
import java.util.List;
import org.springframework.kafka.core.KafkaTemplate;        

@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
public class SellerProductStrategy implements ProductStrategy<Map<String, Object>> {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Product> kafkaTemplate;

    
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
            kafkaTemplate.send("product-create", product);
            return ProductResult.success(PRODUCT_CREATE_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(PRODUCT_VO.getValue(), e.getMessage());
            return ProductResult.fail(PRODUCT_CREATE_FAIL.getValue(), data);
        }
    }



    @Override
    public ProductResult<Map<String, Object>> updateProduct(ProductReq req, long userId, long productId) {
        Map<String, Object> data = new HashMap<>();
        try{
            ProductDto productDto = ProductDto.from(req, userId);
            
            Optional<Product> product = productRepository.findById(productId);
            if(product.isEmpty()){
                return ProductResult.fail(PRODUCT_NOT_FOUND.getValue(), data);
            }
            if(product.get().getSellerId() != userId){
                return ProductResult.fail(PRODUCT_UPDATE_PERMISSION_DENIED.getValue(), data);
            }
            product.get().setProductName(productDto.getProductName());
            product.get().setPrice(productDto.getPrice());
            product.get().setCompanyName(productDto.getCompanyName());
            product.get().setProductImage(productDto.getProductImage());
            product.get().setDescription(productDto.getDescription());
            product.get().setProductStock(productDto.getProductStock());

            product.get().setDiscountRate(productDto.getDiscountRate());
            product.get().setDeliveryFee(productDto.getDeliveryFee());
            product.get().setMainCategory(productDto.getMainCategory());
            product.get().setSubCategory(productDto.getSubCategory());
            product.get().setSeason(productDto.getSeason());
            productRepository.save(product.get());


            data.put(PRODUCT_VO.getValue(), product.get().toVo());
            kafkaTemplate.send("product-update", product.get());
            return ProductResult.success(PRODUCT_UPDATE_SUCCESS.getValue(), data);
        } catch (Exception e) {
            data.put(PRODUCT_VO.getValue(), e.getMessage());
            return ProductResult.fail(PRODUCT_UPDATE_FAIL.getValue(), data);
        }

    }


    @Override
    public ProductResult<Map<String, Object>> deleteProduct(long productId, long userId) {
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Product> product = productRepository.findById(productId);
            if(product.isEmpty()){
                return ProductResult.fail(PRODUCT_NOT_FOUND.getValue(), data);
            }
            if(product.get().getSellerId() != userId){
                return ProductResult.fail(PRODUCT_DELETE_PERMISSION_DENIED.getValue(), data);
            }
            productRepository.delete(product.get());
            data.put(PRODUCT_VO.getValue(), product.get().toVo());
            kafkaTemplate.send("product-delete", product.get());
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
            List<Product> products = productRepository.findAllBySellerId(userId);
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
        return role == Role.SELLER;
    }



    
}
