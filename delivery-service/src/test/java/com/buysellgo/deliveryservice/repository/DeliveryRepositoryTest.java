package com.buysellgo.deliveryservice.repository;

import com.buysellgo.deliveryservice.entity.Delivery;
import com.buysellgo.deliveryservice.entity.Delivery.DeliveryStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.Timestamp;
import java.time.Instant;

import static com.buysellgo.deliveryservice.entity.Delivery.DeliveryStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    @DisplayName("배송관리 테이블/데이터 생성 테스트")
    void createDeliveryTest() {
        // given
        Long orderId = 1L;
        DeliveryStatus deliveryStatus = IN_DELIVERY;

        // when
        Delivery delivery = deliveryRepository.save(Delivery.of(orderId, Timestamp.from(Instant.now()), deliveryStatus));

        // then
        assertNotNull(delivery);
    }
}