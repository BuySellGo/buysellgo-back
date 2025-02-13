package com.buysellgo.statisticsservice.common.configs;

import com.buysellgo.statisticsservice.dto.AccessStatRequestDto;
import com.buysellgo.statisticsservice.dto.SalesStatRequestDto;
import com.buysellgo.statisticsservice.service.AccessStatisticsService;
import com.buysellgo.statisticsservice.service.SalesStatisticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final AccessStatisticsService accessStatisticsService;
    private final SalesStatisticsService salesStatisticsService;

    @KafkaListener(topics = "access-statistics", groupId = "statistics-group")
    public void consumeAccessRecord(String accessStatMessage) {
        // 메시지를 JSON으로 변환하여 DB에 저장
        try {
            AccessStatRequestDto accessStatRequestDto
                    = new ObjectMapper().readValue(accessStatMessage, AccessStatRequestDto.class);

            accessStatisticsService.addAccessRecord(accessStatRequestDto);
            log.info("접속 기록 DB에 저장 완료: {}", accessStatRequestDto);
        } catch (Exception e) {
            log.error("접속 기록 처리 중 오류 발생: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "sales-statistics", groupId = "statistics-group")
    public void consumeSalesRecord(String salesStatMessage) {
        // 메시지를 JSON으로 변환하여 DB에 저장
        try {
            SalesStatRequestDto salesStatRequestDto
                    = new ObjectMapper().readValue(salesStatMessage, SalesStatRequestDto.class);

            salesStatisticsService.addSalesRecord(salesStatRequestDto);
            log.info("매출 발생 기록 DB에 저장 완료: {}", salesStatRequestDto);
        } catch (Exception e) {
            log.error("매출 발생 기록 처리 중 오류 발생: {}", e.getMessage());
        }
    }
}
