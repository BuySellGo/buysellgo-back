package com.buysellgo.promotionservice.repository;

import com.buysellgo.promotionservice.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
