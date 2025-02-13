package com.buysellgo.paymentservice.repository;

import com.buysellgo.paymentservice.entity.PayMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PayMethodRepository extends JpaRepository<PayMethod, Long> {
    List<PayMethod> findAllByUserId(long userId);
    Optional<PayMethod> findByPayMethodName(String payMethodName);
}
