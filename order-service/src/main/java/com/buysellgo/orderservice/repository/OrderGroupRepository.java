package com.buysellgo.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buysellgo.orderservice.entity.OrderGroup;

public interface OrderGroupRepository extends JpaRepository<OrderGroup, Long> {

}
