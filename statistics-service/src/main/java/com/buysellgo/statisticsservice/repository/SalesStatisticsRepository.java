package com.buysellgo.statisticsservice.repository;

import com.buysellgo.statisticsservice.entity.SalesStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesStatisticsRepository extends JpaRepository<SalesStatistics, Long> {

}
