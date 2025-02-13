package com.buysellgo.orderservice.repository;

import java.util.List;
import com.buysellgo.orderservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUserId(long userId);
}
