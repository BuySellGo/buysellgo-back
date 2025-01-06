package com.buysellgo.statisticsservice.repository;

import com.buysellgo.statisticsservice.entity.SalesStatistics;
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
class SalesStatisticsRepositoryTest {

    @Autowired
    private SalesStatisticsRepository salesStatisticsRepository;

    @Test
    @DisplayName("매출통계 테이블/데이터 생성 테스트")
    void createSalesStatisticsTest() {
        // given
        Long sellerId = 1L;
        Long salesAmount = 30000000L;

        // when
        SalesStatistics salesStatistics = salesStatisticsRepository.save(SalesStatistics.of(sellerId, salesAmount));

        // then
        assertNotNull(salesStatistics);
    }
}