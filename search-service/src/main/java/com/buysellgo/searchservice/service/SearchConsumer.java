package com.buysellgo.searchservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.buysellgo.searchservice.document.ProductDocument;
import com.buysellgo.searchservice.repository.ProductDocumentRepository;
import com.buysellgo.searchservice.service.dto.ProductMessage;
import com.buysellgo.searchservice.service.dto.ServiceResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.buysellgo.searchservice.common.util.CommonConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchConsumer {
    private final ObjectMapper objectMapper;
    private final ProductDocumentRepository productDocumentRepository;


    @KafkaListener(topics = "product-create", groupId = "search-group")
    public ServiceResult<Map<String, Object>> listen(String message) {
        Map<String, Object> data = new HashMap<>();
        try {
            ProductMessage productMessage = objectMapper.readValue(message, ProductMessage.class);
            log.info("Received product: {}", productMessage);
            ProductDocument productDocument = ProductDocument.from(productMessage);
            productDocumentRepository.save(productDocument);
            log.info("Successfully added product document: {}", productDocument);
            data.put(PRODUCT_DOCUMENT_CREATE_SUCCESS.getValue(), productDocument);
            return ServiceResult.success(PRODUCT_DOCUMENT_CREATE_SUCCESS.getValue(), data);
        } catch (JsonProcessingException e) {
           log.error(e.getMessage());
           data.put(PRODUCT_DOCUMENT_CREATE_FAIL.getValue(), e.getMessage());
           return ServiceResult.fail(PRODUCT_DOCUMENT_CREATE_FAIL.getValue(), data);
        }

    }

    @KafkaListener(topics = "product-update", groupId = "search-group")
    public ServiceResult<Map<String, Object>> listenUpdate(String message) {
        Map<String, Object> data = new HashMap<>();
        try {
            ProductMessage productMessage = objectMapper.readValue(message, ProductMessage.class);
            log.info("Received product: {}", productMessage);
            ProductDocument newDocument = ProductDocument.from(productMessage);

            Optional<ProductDocument> existingDocument = productDocumentRepository.findByProductId(productMessage.productId());
            if (!existingDocument.isPresent()) {
                data.put(PRODUCT_DOCUMENT_NOT_FOUND.getValue(), productMessage);
                return ServiceResult.fail(PRODUCT_DOCUMENT_NOT_FOUND.getValue(), data);
            } 
            ProductDocument productDocument = existingDocument.get();
            productDocument.setProductName(newDocument.getProductName());
            productDocument.setPrice(newDocument.getPrice());
            productDocument.setCompanyName(newDocument.getCompanyName());
            productDocument.setSellerId(newDocument.getSellerId());
            productDocument.setProductImage(newDocument.getProductImage());
            productDocument.setDescription(newDocument.getDescription());
            productDocument.setProductStock(newDocument.getProductStock());
            productDocument.setDiscountRate(newDocument.getDiscountRate());
            productDocument.setDeliveryFee(newDocument.getDeliveryFee());
            productDocument.setMainCategory(newDocument.getMainCategory());
            productDocument.setSubCategory(newDocument.getSubCategory());
            productDocument.setSeason(newDocument.getSeason());
            productDocumentRepository.save(productDocument);
            data.put(PRODUCT_DOCUMENT_UPDATE_SUCCESS.getValue(), productDocument);
            return ServiceResult.success(PRODUCT_DOCUMENT_UPDATE_SUCCESS.getValue(), data);


        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            data.put(PRODUCT_DOCUMENT_UPDATE_FAIL.getValue(), e.getMessage());
            return ServiceResult.fail(PRODUCT_DOCUMENT_UPDATE_FAIL.getValue(), data);
        }
    }

    @KafkaListener(topics = "product-delete", groupId = "search-group")
    public ServiceResult<Map<String, Object>> listenDelete(String message) {
        Map<String, Object> data = new HashMap<>();
        try {
            ProductMessage productMessage = objectMapper.readValue(message, ProductMessage.class);
            log.info("Received product: {}", productMessage);   
            if (!productDocumentRepository.existsByProductId(productMessage.productId())) {
                data.put(PRODUCT_DOCUMENT_NOT_FOUND.getValue(), productMessage);
                return ServiceResult.fail(PRODUCT_DOCUMENT_NOT_FOUND.getValue(), data);
            }
            productDocumentRepository.deleteByProductId(productMessage.productId());
            data.put(PRODUCT_DOCUMENT_DELETE_SUCCESS.getValue(), productMessage);
            return ServiceResult.success(PRODUCT_DOCUMENT_DELETE_SUCCESS.getValue(), data);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            data.put(PRODUCT_DOCUMENT_DELETE_FAIL.getValue(), e.getMessage());
            return ServiceResult.fail(PRODUCT_DOCUMENT_DELETE_FAIL.getValue(), data);
        }
    }
}
