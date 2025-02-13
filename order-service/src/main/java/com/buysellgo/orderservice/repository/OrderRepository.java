package com.buysellgo.orderservice.repository;

import com.buysellgo.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(long userId, Sort sort);
}
