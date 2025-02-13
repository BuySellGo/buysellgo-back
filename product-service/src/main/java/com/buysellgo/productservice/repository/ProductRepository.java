package com.buysellgo.productservice.repository;

import com.buysellgo.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);
    List<Product> findAllBySellerId(long sellerId);
}
