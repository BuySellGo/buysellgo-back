package com.buysellgo.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buysellgo.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
