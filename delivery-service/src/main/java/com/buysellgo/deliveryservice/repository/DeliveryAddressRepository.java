package com.buysellgo.deliveryservice.repository;

import com.buysellgo.deliveryservice.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

    // userId로 모든 DeliveryAddress 조회
    List<DeliveryAddress> findByUserId(Long userId);

    // userId로 해당하는 DeliveryAddress의 개수 조회
    Long countByUserId(Long userId);
}
