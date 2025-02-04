package com.buysellgo.paymentservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.buysellgo.paymentservice.entity.PayMethod;
import com.buysellgo.paymentservice.entity.PayMethodType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PayMethodRepositoryTest {
    @Autowired
    private PayMethodRepository payMethodRepository;

    @BeforeEach
    void setUp(){
        payMethodRepository.deleteAll();
    }

    @Test
    @DisplayName("결제 수단을 등록한다.")
    void createPayMethod(){
        PayMethod payMethod = PayMethod.of(1, "credit_card", PayMethodType.CREDIT_CARD, "1234567890");
        payMethodRepository.save(payMethod);
        assertEquals(1, payMethodRepository.findAll().size());
    }

    @Test
    @DisplayName("결제 수단을 조회한다.")
    void getPayMethod(){
        createPayMethod();
        PayMethod payMethod = payMethodRepository.findAll().get(0);
        assertEquals(1, payMethod.getPayMethodId());
    }   

    @Test
    @DisplayName("결제 수단을 수정한다.")
    void updatePayMethod(){
        createPayMethod();
        PayMethod payMethod = payMethodRepository.findAll().get(0);
        payMethod.setPayMethodName("credit_card_2");
        payMethodRepository.save(payMethod);
        assertEquals("credit_card_2", payMethod.getPayMethodName());
    }

    @Test
    @DisplayName("결제 수단을 삭제한다.")
    void deletePayMethod(){
        createPayMethod();
        payMethodRepository.deleteAll();
        assertEquals(0, payMethodRepository.findAll().size());
    }
}