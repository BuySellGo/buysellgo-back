package com.buysellgo.paymentservice.repository;

import com.buysellgo.paymentservice.entity.PayMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayMethodRepository extends JpaRepository<PayMethod, Long> {
}
