package com.buysellgo.statisticsservice.repository;

import com.buysellgo.statisticsservice.entity.AccessStatistics;
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
class AccessStatisticsRepositoryTest {

    @Autowired
    private AccessStatisticsRepository accessStatisticsRepository;

    @Test
    @DisplayName("접속통계 테이블/데이터 생성 테스트")
    void createAccessStatisticsTest() {
        // given
        Long userId = 1L;
        String accessIp = "10.10.2.131";

        // when
        AccessStatistics accessStatistics = accessStatisticsRepository.save(AccessStatistics.of(userId, accessIp));

        // then
        assertNotNull(accessStatistics);
    }
}