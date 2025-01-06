package com.buysellgo.deliveryservice.repository;

import com.buysellgo.deliveryservice.entity.DeliveryEndNotification;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
class DeliverEndNotificationRepositoryTest {

    @Autowired
    private DeliveryEndNotificationRepository deliverEndNotificationRepository;

    @Test
    @DisplayName("배송완료 알림 테이블/데이터 생성 테스트")
    void createDeliveryEndNotificationTest() {
        // given
        Long deliveryEndNotificationId = 1L;
        Long profileId = 1L;
        String notiContent = "집으로 배송완료";

        // when
        DeliveryEndNotification deliveryEndNotification
                = deliverEndNotificationRepository.save(
                        DeliveryEndNotification.of(profileId, notiContent,
                                Timestamp.from(Instant.now()),
                                Timestamp.from(Instant.now().plus(Duration.ofDays(1)))));

        // then
        assertNotNull(deliveryEndNotification);
    }
}