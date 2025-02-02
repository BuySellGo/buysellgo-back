package com.buysellgo.promotionservice.common.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Configuration
@EnableScheduling
@Slf4j
public class BatchSchedulerConfig {
    private final JobLauncher jobLauncher;
    private final Job couponIssuanceJob;

/*
    * * * * * *
    초 (0-59)
    분 (0-59)
    시 (0-23)
    일 (1-31)
    월 (1-12)
    요일 (0-6, 0 = 일요일)
 */
    @Scheduled(cron = "* */30 * * * *")
    public void executeBatchJob(){
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(couponIssuanceJob, jobParameters);
            log.info("Batch job 완료...");
        } catch (Exception e) {
            log.error("Batch job 실패: {}", e.getMessage());
        }
    }
}
