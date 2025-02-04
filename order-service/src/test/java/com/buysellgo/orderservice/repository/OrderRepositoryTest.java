package com.buysellgo.orderservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.buysellgo.orderservice.entity.Order;
import com.buysellgo.orderservice.entity.OrderStatus;
import com.buysellgo.orderservice.entity.PaymentMethod;
import com.buysellgo.orderservice.entity.OrderGroup;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderGroupRepository orderGroupRepository;

    @BeforeEach
    void setUp(){
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("주문을 생성해본다.")
    void createOrder(){
        OrderGroup orderGroup = orderGroupRepository.save(OrderGroup.of(1));
        orderRepository.save(Order.of(1, "product1", 1, "company1", 1, 1, 10000, "memo", PaymentMethod.CREDIT_CARD, orderGroup.getGroupId()));
        assertEquals(1, orderRepository.findAll().size());
    }


    @Test
    @DisplayName("주문을 조회해본다.")
    void getOrder(){
        createOrder();
        Order order = orderRepository.findAll().get(0);
        assertEquals(1, order.getProductId());
    }

    @Test
    @DisplayName("주문을 수정해본다.")
    void updateOrder(){
        createOrder();
        Order order = orderRepository.findAll().get(0);
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    @DisplayName("주문을 삭제해본다.")
    void deleteOrder(){
        createOrder();
        Order order = orderRepository.findAll().get(0);
        orderRepository.delete(order);
        assertEquals(0, orderRepository.findAll().size());
    }
}