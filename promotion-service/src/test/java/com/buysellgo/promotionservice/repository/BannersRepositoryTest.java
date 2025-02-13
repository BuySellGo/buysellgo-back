package com.buysellgo.promotionservice.repository;

import com.buysellgo.promotionservice.entity.Banners;
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
@Rollback(value = true)
@Transactional
class BannersRepositoryTest {

    @Autowired
    private BannersRepository bannersRepository;

    @Test
    @DisplayName("배너 테이블/데이터 생성 테스트")
    void createBannersTest() {
        // given
        String bannerTitle = "봄 맞이 할인 행사";
        LocalDateTime startDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime endDate = startDate.plusDays(3);
        String imageUrl = "https://www.google.com/images/branding/google_logo.png";
        String productUrl = "/promotion/product/100";

        // when
        Banners banners = bannersRepository.save(Banners.of(bannerTitle, startDate, endDate, imageUrl, productUrl));

        // then
        assertNotNull(banners);
    }
}