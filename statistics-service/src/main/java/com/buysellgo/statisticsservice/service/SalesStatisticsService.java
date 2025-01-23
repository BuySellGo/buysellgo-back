package com.buysellgo.statisticsservice.service;

import com.buysellgo.statisticsservice.common.auth.TokenUserInfo;
import com.buysellgo.statisticsservice.common.entity.Role;
import com.buysellgo.statisticsservice.dto.SalesStatRequestDto;
import com.buysellgo.statisticsservice.entity.QSalesStatistics;
import com.buysellgo.statisticsservice.entity.SalesStatistics;
import com.buysellgo.statisticsservice.repository.SalesStatisticsRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SalesStatisticsService {
    private final SalesStatisticsRepository salesStatisticsRepository;
    private final JPAQueryFactory jpaQueryFactory;


    public SalesStatistics.Vo addSalesRecord(SalesStatRequestDto salesStatRequestDto) {
        SalesStatistics salesStatistics = salesStatisticsRepository.save(SalesStatistics.of(
                salesStatRequestDto.getSellerId(),
                salesStatRequestDto.getCreatedAt(),
                salesStatRequestDto.getSalesAmount()
        ));

        return salesStatistics.toVo();
    }

    public MonthlySalesStatisticsReport getMonthlySalesStatisticsReport(
            int year, int month, TokenUserInfo tokenUserInfo) {

        // 월의 시작/마지막날 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        log.info("startDate: {}", startDate);
        log.info("endDate: {}", endDate);

        // 총 매출액
        Long totalSalesAmount = getTotalSalesAmount(startDate, endDate, tokenUserInfo);
        log.info("totalSalesAmount: {}", totalSalesAmount);

        // Daily 통계
        List<DailyStat> dailyStatList = getDailyStats(startDate, endDate, tokenUserInfo);
        log.info("dailyStatList: {}", dailyStatList);

        // Weekly 통계
        List<WeeklyStat> weeklyStatList = getWeeklyStats(startDate, endDate, tokenUserInfo);
        log.info("weeklyStatList: {}", weeklyStatList);

        MonthlySalesStatisticsReport monthlySalesStatisticsReport
                = new MonthlySalesStatisticsReport(totalSalesAmount, dailyStatList, weeklyStatList);

        return monthlySalesStatisticsReport;

    }


    private Long getTotalSalesAmount(LocalDate startDate, LocalDate endDate, TokenUserInfo tokenUserInfo) {

        QSalesStatistics qSalesStatistics = QSalesStatistics.salesStatistics;

        log.info("getTotalSalesAmount ======================> ");

        JPAQuery<Long> sumQuery;

        if (tokenUserInfo.getRole() == Role.SELLER) {
            sumQuery = jpaQueryFactory
                    .select(qSalesStatistics.salesAmount.sum())
                    .from(qSalesStatistics)
                    .where(qSalesStatistics.sellerId.eq(tokenUserInfo.getId()));
        } else {
            sumQuery = jpaQueryFactory
                    .select(qSalesStatistics.salesAmount.sum())
                    .from(qSalesStatistics);
        }

        Long totalSalesAmount = sumQuery.fetchOne();

        totalSalesAmount = totalSalesAmount == null ? 0 : totalSalesAmount;

        return totalSalesAmount;
    }

    private List<DailyStat> getDailyStats(LocalDate startDate, LocalDate endDate, TokenUserInfo tokenUserInfo) {
        QSalesStatistics qSalesStatistics = QSalesStatistics.salesStatistics;

        JPAQuery<Tuple> query;

        if (tokenUserInfo.getRole() == Role.SELLER) {
            query = jpaQueryFactory
                    .select(
                            Expressions.template(
                                    Date.class, "DATE({0})",
                                    qSalesStatistics.createdAt),
                            qSalesStatistics.salesAmount.sum()
                    )
                    .from(qSalesStatistics)
                    .where(qSalesStatistics.sellerId.eq(tokenUserInfo.getId()))
                    .where(qSalesStatistics.createdAt.between(
                            Timestamp.valueOf(startDate.atStartOfDay()),
                            Timestamp.valueOf(endDate.atTime(23, 59, 59))
                    ))
                    .groupBy(
                            Expressions.template(
                                    Date.class, "DATE({0})",
                                    qSalesStatistics.createdAt
                            ));
        } else {

            query = jpaQueryFactory
                    .select(
                            Expressions.template(
                                    Date.class, "DATE({0})",
                                    qSalesStatistics.createdAt),
                            qSalesStatistics.salesAmount.sum()
                    )
                    .from(qSalesStatistics)
                    .where(qSalesStatistics.createdAt.between(
                            Timestamp.valueOf(startDate.atStartOfDay()),
                            Timestamp.valueOf(endDate.atTime(23, 59, 59))
                    ))
                    .groupBy(
                            Expressions.template(
                                    Date.class, "DATE({0})",
                                    qSalesStatistics.createdAt
                            ));
        }

        List<Tuple> tupleList = query.fetch();

        // 결과 반환
        List<DailyStat> dailyStatList = tupleList.stream()
                .map(result -> new DailyStat(
                        result.get(0, Date.class).toLocalDate(),
                        result.get(1, Long.class)))
                .collect(Collectors.toList());

        return dailyStatList;

    }

    private List<WeeklyStat> getWeeklyStats(LocalDate startDate, LocalDate endDate, TokenUserInfo tokenUserInfo) {
        QSalesStatistics qSalesStatistics = QSalesStatistics.salesStatistics;

        if (tokenUserInfo.getRole() == Role.SELLER) {
            JPAQuery<Tuple> query = jpaQueryFactory
                    .select(
                            qSalesStatistics.createdAt.week(),
                            qSalesStatistics.salesAmount.sum()
                    )
                    .from(qSalesStatistics)
                    .where(qSalesStatistics.sellerId.eq(tokenUserInfo.getId()))
                    .where(qSalesStatistics.createdAt.between(
                            Timestamp.valueOf(startDate.atStartOfDay()),
                            Timestamp.valueOf(endDate.atTime(23, 59, 59))
                    ))
                    .groupBy(qSalesStatistics.createdAt.week());   // Week 를 기준으로 그룹화
        }

        JPAQuery<Tuple> query = jpaQueryFactory
                .select(
                        qSalesStatistics.createdAt.week(),
                        qSalesStatistics.salesAmount.sum()
                )
                .from(qSalesStatistics)
                .where(qSalesStatistics.createdAt.between(
                        Timestamp.valueOf(startDate.atStartOfDay()),
                        Timestamp.valueOf(endDate.atTime(23, 59, 59))
                ))
                .groupBy(qSalesStatistics.createdAt.week());   // Week 를 기준으로 그룹화

        List<Tuple> tupleList = query.fetch();

        // 결과 변환
        List<WeeklyStat> weeklyStatList = tupleList.stream()
                .map(result -> new WeeklyStat(
                        result.get(0, Integer.class),  // 주 변환
                        result.get(1, Long.class)))    // 총매출 변환
                .collect(Collectors.toList());

        return weeklyStatList;
    }

    // 이하 DTO 클래스
    @Getter
    @Setter
    public static class MonthlySalesStatisticsReport {
        private final Long totalSalesAmount;
        private final List<DailyStat> dailyStatList;
        private final List<WeeklyStat> weeklyStatList;

        public MonthlySalesStatisticsReport(
                Long totalSalesAmount,
                List<DailyStat> dailyStatList,
                List<WeeklyStat> weeklyStatList) {

            this.totalSalesAmount = totalSalesAmount;
            this.dailyStatList = dailyStatList;
            this.weeklyStatList = weeklyStatList;
        }
    }

    @Getter
    @Setter
    public static class DailyStat {
        private final LocalDate date;
        private final Long sumSalesAmount;

        public DailyStat(LocalDate date, Long sumSalesAmount) {
            this.date = date;
            this.sumSalesAmount = sumSalesAmount;
        }
    }

    @Getter
    @Setter
    public static class WeeklyStat {
        private final Integer weekNumber;
        private final Long sumSalesAmount;

        public WeeklyStat(Integer weekNumber, Long sumSalesAmount) {
            this.weekNumber = weekNumber;
            this.sumSalesAmount = sumSalesAmount;
        }
    }

}
