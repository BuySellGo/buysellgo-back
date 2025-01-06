package com.buysellgo.promotionservice.repository;

import com.buysellgo.promotionservice.entity.Banners;
import com.buysellgo.promotionservice.entity.Promotion;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
class PromotionRepositoryTest {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private BannersRepository bannersRepository;

    @Test
    @DisplayName("프로모션 테이블/데이터 생성 테스트")
    void createPromotionTest() {
        // given
        String bannerTitle = "봄 맞이 할인 행사";
        LocalDateTime startDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime endDate = startDate.plusDays(3);
        String imageUrl = "https://www.google.com/images/branding/google_logo.png";
        String productUrl = "/promotion/product/100";

        Long sellerId = 1L;
        Long productId = 1L;
        Banners banners = bannersRepository.save(Banners.of(bannerTitle, startDate, endDate, imageUrl, productUrl));
        Integer discountRate = 50;
        Boolean isApproved = true;

        // when
        Promotion promotion = promotionRepository.save(Promotion.of(sellerId, productId, banners, discountRate, startDate, endDate, isApproved));

        // then
        assertNotNull(promotion);
    }
}