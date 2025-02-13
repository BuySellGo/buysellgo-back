package com.buysellgo.searchservice.repository; 

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.buysellgo.searchservice.document.ProductDocument;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;  
@Repository
public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findByProductNameContaining(String productName);
    Optional<ProductDocument> findByProductId(Long productId);
    List<ProductDocument> findByMainCategoryContaining(String mainCategory);
    List<ProductDocument> findBySubCategoryContaining(String subCategory);
    boolean existsByProductId(Long productId);
    void deleteByProductId(Long productId); 
    List<ProductDocument> findBySeasonContaining(String season);
}


