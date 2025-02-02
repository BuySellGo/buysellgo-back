package com.buysellgo.promotionservice.common.configs;

import com.buysellgo.promotionservice.common.dto.CouponIssuanceNotificationDTO;
import com.buysellgo.promotionservice.entity.Coupon;
import com.buysellgo.promotionservice.entity.CouponIssuance;
import com.buysellgo.promotionservice.entity.CouponNotification;
import com.buysellgo.promotionservice.repository.CouponIssuanceRepository;
import com.buysellgo.promotionservice.repository.CouponNotificationRepository;
import com.buysellgo.promotionservice.repository.PaymentHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
//@EnableBatchProcessing
@RequiredArgsConstructor
@Configuration
@Slf4j
public class CouponBatchConfig {
    private final CouponIssuanceRepository couponIssuanceRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final CouponNotificationRepository couponNotificationRepository;


    @Bean
    public Job couponIssuanceJob(JobRepository jobRepository,
                                 PlatformTransactionManager platformTransactionManager) {

        log.info("JobBuilder =============> couponIssuanceJob()");

        return new JobBuilder("couponIssuanceJob", jobRepository)
                .start(couponIssuanceStep(jobRepository, platformTransactionManager))
                .build();

    }

    @Bean
    public Step couponIssuanceStep(JobRepository jobRepository,
                                   PlatformTransactionManager platformTransactionManager) {

        log.info("StepBuilder =============> couponIssuanceStep()");

        return new StepBuilder("couponIssuanceStep", jobRepository)
                .<Long, CouponIssuanceNotificationDTO>chunk(1, platformTransactionManager)
                .reader(userWithMoreThanTenOrdersReader())
                .processor(userToCouponProcessor())
                .writer(couponIssuanceAndNotificationWriter())
                .transactionManager(platformTransactionManager)
                .build();
    }

    @Bean
    public ItemReader<Long> userWithMoreThanTenOrdersReader() {

        log.info("ItemReader =============> userWithMoreThanTenOrdersReader()");

        return new ItemReader<Long>() {
            private Iterator<Long> userIterator;

            @Override
            public Long read() throws Exception {

                if (userIterator == null) {
                    // 이번 달 1일과 현재 날짜를 기준으로 조회
                    LocalDate startDate = LocalDate.now().withDayOfMonth(1);
                    LocalDate endDate = LocalDate.now();


                    try {
                        // 결과를 가져와서 iterator로 변환
                        List<Object[]> results
                                = paymentHistoryRepository.findUsersWithMoreThanTenOrdersThisMonth(
                                Timestamp.valueOf(startDate.atStartOfDay()),
                                Timestamp.valueOf(endDate.atTime(23, 59, 59)));

                        log.info("대상자: results: {} ", results.size());

                        // 결과에서 첫번째 값만 추출하여 Iterator로 만듦
                        userIterator = results.stream()
                                .map(result -> (Long) result[0])
                                .collect(Collectors.toList())
                                .iterator();
                    } catch (Exception e) {
                        log.error("Error occurred while fetching users from DB: {}", e.getMessage());
                        throw new RuntimeException("Error fetching user data", e);
                    }
                }

                if (userIterator.hasNext()) {
                    return userIterator.next();
                } else {
                    return null;
                }
            }
        };
    }

    @Bean
    public ItemProcessor<Long, CouponIssuanceNotificationDTO> userToCouponProcessor() {

        log.info("ItemProcessor =============> userToCouponProcessor()");


        return userId -> {
            CouponIssuanceNotificationDTO couponIssuanceNotificationDTO
                    = new CouponIssuanceNotificationDTO();
            Coupon coupon = Coupon.builder().id(1L).build();

            couponIssuanceNotificationDTO.setUserId(userId);
            couponIssuanceNotificationDTO.setCouponIssuance(CouponIssuance.of(userId, coupon));
            couponIssuanceNotificationDTO.setCouponNotification(
                    CouponNotification.of(
                            userId,
                            "구매3건 충족 쿠폰이 발급 되었습니다.",
                            Timestamp.from(Instant.now())
                    ));
            return couponIssuanceNotificationDTO;
        };
    }

    @Bean
    public ItemWriter<CouponIssuanceNotificationDTO> couponIssuanceAndNotificationWriter() {

        log.info("ItemWriter =============> couponIssuanceAndNotificationWriter()");

        return items -> {
            for (CouponIssuanceNotificationDTO dto : items) {
                // coupon_issuance &  coupon_notification 테이블에 데이터 저장
                log.info("ItemWriter =============> coupon_issuance &  coupon_notification 테이블에 데이터 저장");

                couponIssuanceRepository.save(dto.getCouponIssuance());
                couponNotificationRepository.save(dto.getCouponNotification());
            }
        };
    }


}
