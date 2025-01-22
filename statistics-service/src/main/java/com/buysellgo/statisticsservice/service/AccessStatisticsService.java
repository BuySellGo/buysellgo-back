package com.buysellgo.statisticsservice.service;

import com.buysellgo.statisticsservice.entity.AccessStatistics;
import com.buysellgo.statisticsservice.repository.AccessStatisticsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccessStatisticsService {
    private final AccessStatisticsRepository accessStatisticsRepository;

    public AccessStatistics addAccessRecord(Long userId, String accessIp) {

        AccessStatistics accessStatistics
                = accessStatisticsRepository.save(AccessStatistics.of(userId, accessIp));

        return accessStatistics;

    }

    public void getMonthlyAccessStats(int year, int month) {

        // 월의 시작/마지막 날 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Daily Stat
        List<AccessStatistics> dailyStats
                = accessStatisticsRepository.findByAccessDateTimeBetween(
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay());

    }
}
