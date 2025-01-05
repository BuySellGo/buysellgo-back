package com.buysellgo.promotionservice.repository;

import com.buysellgo.promotionservice.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

}
