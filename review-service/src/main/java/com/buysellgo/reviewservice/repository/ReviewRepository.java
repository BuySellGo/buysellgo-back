package com.buysellgo.reviewservice.repository;

import com.buysellgo.reviewservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserId(long userId);
    List<Review> findAllByUserId(long userId);
    Optional<Review> findByOrderId(long orderId);
    boolean existsByOrderId(long orderId);
    List<Review> findAllBySellerId(long sellerId);
    List<Review> findAllByProductId(long productId);
}
