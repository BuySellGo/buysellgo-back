package com.buysellgo.deliveryservice.repository;

import com.buysellgo.deliveryservice.entity.Delivery;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

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

        // when
        Delivery delivery = deliveryRepository.save(Delivery.of(orderId, Delivery.DeliveryStatus.IN_DELIVERY));

        // then
        assertNotNull(delivery);
    }
}