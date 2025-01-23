package com.buysellgo.statisticsservice.repository;

import com.buysellgo.statisticsservice.entity.AccessStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface AccessStatisticsRepository extends JpaRepository<AccessStatistics, Long> {

//    // 특정 연도와 월의 데이터를 조회 (일별 통계)
//    List<AccessStatistics> findByAccessDateTimeBetween(Timestamp startDate, Timestamp endDate);
//
//    // 특정 연도와 월의 데이터를 조회 (주별 통계)
//    List<AccessStatistics> findByAccessDateTimeBetweenAndAccessDateTimeAfter(Timestamp startDate, Timestamp endDate);
}
