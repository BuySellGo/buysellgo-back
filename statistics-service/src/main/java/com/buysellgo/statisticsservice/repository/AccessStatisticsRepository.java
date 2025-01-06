package com.buysellgo.statisticsservice.repository;

import com.buysellgo.statisticsservice.entity.AccessStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessStatisticsRepository extends JpaRepository<AccessStatistics, Long> {

}
