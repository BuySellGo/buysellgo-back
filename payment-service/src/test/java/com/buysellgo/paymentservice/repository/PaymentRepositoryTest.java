package com.buysellgo.paymentservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.buysellgo.paymentservice.entity.Payment;
import com.buysellgo.paymentservice.entity.PaymentStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp(){
        paymentRepository.deleteAll();
    }

    @Test
    @DisplayName("결제를 생성한다.")
    void createPayment(){
        Payment payment = Payment.of(1, 1, 1, 10000, "CREDIT_CARD_NAME", 1);
        paymentRepository.save(payment);
        assertEquals(1, paymentRepository.findAll().size());
    }

    @Test
    @DisplayName("결제를 조회한다.")
    void getPayment(){
        createPayment();
        Payment payment = paymentRepository.findAll().get(0);
        assertEquals(1, payment.getOrderId());
    }

    @Test
    @DisplayName("결제를 수정한다.")
    void updatePayment(){
        createPayment();
        Payment payment = paymentRepository.findAll().get(0);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
        assertEquals(PaymentStatus.SUCCESS, payment.getPaymentStatus());
    }

    @Test
    @DisplayName("결제를 삭제한다.")
    void deletePayment(){
        createPayment();
        paymentRepository.deleteAll();
        assertEquals(0, paymentRepository.findAll().size());
    }
}