package com.buysellgo.searchservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.buysellgo.searchservice.document.ProductDocument;
import com.buysellgo.searchservice.repository.ProductDocumentRepository;
import com.buysellgo.searchservice.service.dto.ServiceResult;
import static com.buysellgo.searchservice.common.util.CommonConstant.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional


public class SearchService {
    private final ProductDocumentRepository productDocumentRepository;

    public ServiceResult<Map<String, Object>> searchProducts(String keyword) {
        Map<String, Object> data = new HashMap<>();
        try {
            List<ProductDocument> productDocuments = productDocumentRepository.findByProductNameContaining(keyword);
            data.put(PRODUCT_DOCUMENT_SEARCH_SUCCESS.getValue(), productDocuments);
            return ServiceResult.success(PRODUCT_DOCUMENT_SEARCH_SUCCESS.getValue(), data);
        } catch (Exception e) {
            log.error(e.getMessage());
            data.put(PRODUCT_DOCUMENT_SEARCH_FAIL.getValue(), e.getMessage());
            return ServiceResult.fail(PRODUCT_DOCUMENT_SEARCH_FAIL.getValue(), data);
        }
    }



    public ServiceResult<Map<String, Object>> getMainCategories(String keyword) {
        Map<String, Object> data = new HashMap<>();
        try {
            List<ProductDocument> productDocuments = productDocumentRepository.findByMainCategoryContaining(keyword);
            data.put(PRODUCT_DOCUMENT_MAIN_CATEGORY_SEARCH_SUCCESS.getValue(), productDocuments);
            return ServiceResult.success(PRODUCT_DOCUMENT_MAIN_CATEGORY_SEARCH_SUCCESS.getValue(), data);
        } catch (Exception e) {
            log.error(e.getMessage());
            data.put(PRODUCT_DOCUMENT_MAIN_CATEGORY_SEARCH_FAIL.getValue(), e.getMessage());
            return ServiceResult.fail(PRODUCT_DOCUMENT_MAIN_CATEGORY_SEARCH_FAIL.getValue(), data);
        }
    }


    public ServiceResult<Map<String, Object>> getSubCategories(String keyword) {
        Map<String, Object> data = new HashMap<>();
        try {
            List<ProductDocument> productDocuments = productDocumentRepository.findBySubCategoryContaining(keyword);
            data.put(PRODUCT_DOCUMENT_SUB_CATEGORY_SEARCH_SUCCESS.getValue(), productDocuments);
            return ServiceResult.success(PRODUCT_DOCUMENT_SUB_CATEGORY_SEARCH_SUCCESS.getValue(), data);
        } catch (Exception e) {
            log.error(e.getMessage());
            data.put(PRODUCT_DOCUMENT_SUB_CATEGORY_SEARCH_FAIL.getValue(), e.getMessage());
            return ServiceResult.fail(PRODUCT_DOCUMENT_SUB_CATEGORY_SEARCH_FAIL.getValue(), data);
        }
    }

    public ServiceResult<Map<String, Object>> getSeasons(String keyword) {
        Map<String, Object> data = new HashMap<>();
        try {
            List<ProductDocument> productDocuments = productDocumentRepository.findBySeasonContaining(keyword);
            data.put(PRODUCT_DOCUMENT_SEASON_SEARCH_SUCCESS.getValue(), productDocuments);
            return ServiceResult.success(PRODUCT_DOCUMENT_SEASON_SEARCH_SUCCESS.getValue(), data);
        } catch (Exception e) {
            log.error(e.getMessage());
            data.put(PRODUCT_DOCUMENT_SEASON_SEARCH_FAIL.getValue(), e.getMessage());
            return ServiceResult.fail(PRODUCT_DOCUMENT_SEASON_SEARCH_FAIL.getValue(), data);
        }
    }


}
