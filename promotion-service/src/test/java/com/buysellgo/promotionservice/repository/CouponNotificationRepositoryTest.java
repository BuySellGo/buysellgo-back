package com.buysellgo.promotionservice.repository;

import com.buysellgo.promotionservice.entity.CouponNotification;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
class CouponNotificationRepositoryTest {

    @Autowired
    private CouponNotificationRepository couponNotificationRepository;

    @Test
    @DisplayName("쿠폰 알림 테이블/데이터 생성 테스트")
    void createCouponNotificationTest() {
        // given
        Long couponNotificationId = 1L;
        Long profileId = 1L;
        String notiContent = "80% 할인 쿠폰 발급";

        // when
        CouponNotification couponNotification = couponNotificationRepository.save(CouponNotification.of(profileId, notiContent, Timestamp.from(Instant.now()), Timestamp.from(Instant.now().plus(Duration.ofDays(1)))));

        // then
        assertNotNull(couponNotification);
    }

}