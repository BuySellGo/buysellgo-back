package com.buysellgo.productservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.buysellgo.productservice.entity.MainCategory;
import com.buysellgo.productservice.entity.SubCategory;
import com.buysellgo.productservice.entity.Season;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.buysellgo.productservice.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("상품을 생성해본다.")
    void createProduct() {
        productRepository.save(Product.of("상품1", 10000, "회사1", 1L, "https://example.com/image.jpg", "상품1 설명", 100, 10, 3000, MainCategory.FASHION, SubCategory.WOMENS_CLOTHING, Season.SPRING));
        assertEquals(1, productRepository.findAll().size());
    }

    @Test
    @DisplayName("상품을 조회해본다.")
    void getProduct() {
        createProduct();
        Product product = productRepository.findAll().get(0);
        assertEquals("상품1", product.getProductName());
    }

    @Test
    @DisplayName("상품을 수정해본다.")
    void updateProduct() {
        createProduct();
        Product product = productRepository.findAll().get(0);
        product.setProductName("상품2");
        productRepository.save(product);
        assertEquals("상품2", product.getProductName());
    }

    @Test
    @DisplayName("상품을 삭제해본다.")
    void deleteProduct() {
        createProduct();
        Product product = productRepository.findAll().get(0);
        productRepository.delete(product);
        assertEquals(0, productRepository.findAll().size());
    }
}