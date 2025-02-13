package com.buysellgo.deliveryservice.repository;

import com.buysellgo.deliveryservice.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}
