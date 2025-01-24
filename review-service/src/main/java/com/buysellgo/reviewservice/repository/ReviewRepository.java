package com.buysellgo.reviewservice.repository;

import com.buysellgo.reviewservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserId(long userId);

    boolean existsByOrderId(long orderId);
}
