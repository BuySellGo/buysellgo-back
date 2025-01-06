package com.buysellgo.promotionservice.repository;

import com.buysellgo.promotionservice.entity.Coupon;
import com.buysellgo.promotionservice.entity.CouponIssuance;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
class CouponIssuanceRepositoryTest {

    @Autowired
    private CouponIssuanceRepository couponIssuanceRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("쿠폰 발급내역 테이블/데이터 생성 테스트")
    void createCouponIssuanceTest() {
        // given
        Long profileId = 1L;
        String couponTitle = "VIP 할인 쿠폰";
        Integer discountRate = 80;
        Coupon coupon = couponRepository.save(Coupon.of(couponTitle, discountRate, Coupon.EligibleRecipient.VIP));

        // when
        CouponIssuance couponIssuance = couponIssuanceRepository.save(CouponIssuance.of(profileId, coupon));

        // then
        assertNotNull(couponIssuance);
    }
}