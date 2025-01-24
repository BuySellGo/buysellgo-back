package com.buysellgo.statisticsservice.service;

import com.buysellgo.statisticsservice.dto.AccessStatRequestDto;
import com.buysellgo.statisticsservice.entity.AccessStatistics;
import com.buysellgo.statisticsservice.entity.QAccessStatistics;
import com.buysellgo.statisticsservice.repository.AccessStatisticsRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccessStatisticsService {
    private final AccessStatisticsRepository accessStatisticsRepository;
    private final JPAQueryFactory jpaQueryFactory;

    // 접속 기록
//    public AccessStatistics addAccessRecord(Long userId, String accessIp, Boolean isDto) {
    public AccessStatistics addAccessRecord(AccessStatRequestDto accessStatRequestDto) {

//        AccessStatistics accessStatistics
//                = accessStatisticsRepository.save(
//                        AccessStatistics.of(userId, accessIp, Timestamp.from(Instant.now())));

        AccessStatistics accessStatistics
                = accessStatisticsRepository.save(
                AccessStatistics.of(
                        accessStatRequestDto.getUserId(),
                        accessStatRequestDto.getAccessIp(),
                        accessStatRequestDto.getAccessDateTime()));

        return accessStatistics;

    }

    // 월 접속 통계(일, 주,
    public MonthlyAccessStatisticsReport getMonthlyAccessStats(int year, int month) {

        // 월의 시작/마지막 날 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        log.info("startDate: {}", startDate);
        log.info("endDate: {}", endDate);

        // 총 접속수, 접속자수
        Long totalAccessCount = getTotalAccessCount(startDate, endDate);
        Long totalUserCount = getTotalUserCount(startDate, endDate);
        log.info("totalAccessCount: {}", totalAccessCount);
        log.info("totalUserCount: {}", totalUserCount);

        // Daily 통계
        List<DailyStat> dailyStatList = getDailyStats(startDate, endDate);
        log.info("dailyStatList: {}", dailyStatList);

        // Weekly 통계
        List<WeeklyStat> weeklyStatList = getWeeklyStats(startDate, endDate);
        log.info("weeklyStatList: {}", weeklyStatList);


        MonthlyAccessStatisticsReport monthlyAccessStatisticsReport
                = new MonthlyAccessStatisticsReport(
                totalAccessCount, totalUserCount, dailyStatList, weeklyStatList);

        return monthlyAccessStatisticsReport;
    }

    private Long getTotalAccessCount(LocalDate startDate, LocalDate endDate) {
        QAccessStatistics qAccessStatistics = QAccessStatistics.accessStatistics;

        log.info("getTotalAccessCount ======================> ");

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(qAccessStatistics.count())
                .from(qAccessStatistics)
                .where(qAccessStatistics.accessDateTime.between(
                        Timestamp.valueOf(startDate.atStartOfDay()),
                        Timestamp.valueOf(endDate.atTime(23, 59, 59))
                ));

        Long totalAccessCount = countQuery.fetchOne();

        totalAccessCount = totalAccessCount == null ? 0 : totalAccessCount;

        log.info("totalAccessCount: {}", totalAccessCount);

        return totalAccessCount;
    }

    private Long getTotalUserCount(LocalDate startDate, LocalDate endDate) {
        QAccessStatistics qAccessStatistics = QAccessStatistics.accessStatistics;

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(qAccessStatistics.userId.countDistinct())
                .from(qAccessStatistics)
                .where(qAccessStatistics.accessDateTime.between(
                        Timestamp.valueOf(startDate.atStartOfDay()),
                        Timestamp.valueOf(endDate.atTime(23, 59, 59))
                ));

        Long totalUserCount = countQuery.fetchOne();
        totalUserCount = totalUserCount == null ? 0 : totalUserCount;
        log.info("totalUserCount: {}", totalUserCount);

        return totalUserCount;
    }

    private List<DailyStat> getDailyStats(LocalDate startDate, LocalDate endDate) {
        QAccessStatistics qAccessStatistics = QAccessStatistics.accessStatistics;

        // 날짜별 접속수 및 유니크 IP수
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(
                        Expressions.template(
//                                java.time.LocalDate.class, "DATE({0})",
                                java.sql.Date.class, "DATE({0})",
                                qAccessStatistics.accessDateTime),
                        qAccessStatistics.count(),
                        qAccessStatistics.userId.countDistinct(),
                        qAccessStatistics.accessIp.countDistinct()
                )
                .from(qAccessStatistics)
                .where(qAccessStatistics.accessDateTime.between(
                        Timestamp.valueOf(startDate.atStartOfDay()),
                        Timestamp.valueOf(endDate.atTime(23, 59, 59))
                ))
                .groupBy(   // 날짜를 기준으로 그룹화
                        Expressions.template(
//                                java.time.LocalDate.class, "DATE({0})",
                                java.sql.Date.class, "DATE({0})",
                                qAccessStatistics.accessDateTime));

        List<Tuple> tupleList = query.fetch();

        // 결과 변환
        List<DailyStat> dailyStatList = tupleList.stream()
                .map(result -> new DailyStat(
//                        result.get(0, java.time.LocalDate.class),  // LocalDate로 변환
                        result.get(0, java.sql.Date.class).toLocalDate(),  // LocalDate로 변환
                        result.get(1, Long.class),      // count() 결과를 Long으로 변환
                        result.get(2, Long.class),
                        result.get(3, Long.class)))     // countDistinct() 결과를 Long으로 변환
                .collect(Collectors.toList());

        return dailyStatList;
    }

    private List<WeeklyStat> getWeeklyStats(LocalDate startDate, LocalDate endDate) {
        QAccessStatistics qAccessStatistics = QAccessStatistics.accessStatistics;

        // 날짜별 접속수 및 유니크 IP수
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(
//                        Expressions.template(
//                                java.time.LocalDate.class, "DATE({0})",
//                                qAccessStatistics.accessDateTime),
                        qAccessStatistics.accessDateTime.week(),
                        qAccessStatistics.count(),
                        qAccessStatistics.userId.countDistinct(),
                        qAccessStatistics.accessIp.countDistinct()
                )
                .from(qAccessStatistics)
                .where(qAccessStatistics.accessDateTime.between(
                        Timestamp.valueOf(startDate.atStartOfDay()),
                        Timestamp.valueOf(endDate.atTime(23, 59, 59))
                ))
                .groupBy(qAccessStatistics.accessDateTime.week());   // Week 를 기준으로 그룹화

        List<Tuple> tupleList = query.fetch();

        // 결과 변환
        List<WeeklyStat> weeklyStatList = tupleList.stream()
                .map(result -> new WeeklyStat(
                        result.get(0, Integer.class),  // LocalDate로 변환
                        result.get(1, Long.class),      // count() 결과를 Long으로 변환
                        result.get(2, Long.class),
                        result.get(3, Long.class)))     // countDistinct() 결과를 Long으로 변환
                .collect(Collectors.toList());

        return weeklyStatList;
    }


    // 이하 DTO 클래스
    @Getter
    @Setter
    public static class MonthlyAccessStatisticsReport {
        private final Long totalAccessCount;
        private final Long totalUserCount;
        private final List<DailyStat> dailyStatList;
        private final List<WeeklyStat> weeklyStatList;

        public MonthlyAccessStatisticsReport(
                Long totalAccessCount,
                Long totalUserCount,
                List<DailyStat> dailyStatList,
                List<WeeklyStat> weeklyStatList) {

            this.totalAccessCount = totalAccessCount;
            this.totalUserCount = totalUserCount;
            this.dailyStatList = dailyStatList;
            this.weeklyStatList = weeklyStatList;
        }
    }

    @Getter
    @Setter
    public static class DailyStat {
        private final LocalDate date;
        private final Long accessCount;
        private final Long userCount;
        private final Long uniqueIpCount;

        public DailyStat(LocalDate date, Long accessCount, Long userCount, Long uniqueIpCount) {
            this.date = date;
            this.accessCount = accessCount;
            this.userCount = userCount;
            this.uniqueIpCount = uniqueIpCount;
        }
    }

    @Getter
    @Setter
    public static class WeeklyStat {
        private final Integer weekNumber;
        private final Long accessCount;
        private final Long userCount;
        private final Long uniqueIpCount;

        public WeeklyStat(Integer weekNumber, Long accessCount, Long userCount, Long uniqueIpCount) {
            this.weekNumber = weekNumber;
            this.accessCount = accessCount;
            this.userCount = userCount;
            this.uniqueIpCount = uniqueIpCount;
        }
    }

}
