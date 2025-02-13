package com.buysellgo.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import com.buysellgo.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
    List<Payment> findAllByUserId(long userId);
    Optional<Payment> findByOrderId(long orderId);
}
