package com.buysellgo.promotionservice.repository;

import com.buysellgo.promotionservice.entity.CouponIssuance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponIssuanceRepository extends JpaRepository<CouponIssuance, Long> {

}
