package com.buysellgo.orderservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.buysellgo.orderservice.entity.Cart;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void setUp(){
        cartRepository.deleteAll();
    }

    @Test   
    @DisplayName("장바구니를 생성해본다.")  
    void createCart(){
        cartRepository.save(Cart.of(1, 1, "product1", 1, "company1", 1, 10000));
        assertEquals(1, cartRepository.findAll().size());
    }


    @Test
    @DisplayName("장바구니를 조회해본다.")
    void getCart(){
        createCart();
        Cart cart = cartRepository.findAll().get(0);
        assertEquals(1, cart.getUserId());
    }       

    @Test
    @DisplayName("장바구니를 수정해본다.")
    void updateCart(){
        createCart();
        Cart cart = cartRepository.findAll().get(0);
        cart.setQuantity(2);    
        cartRepository.save(cart);
        assertEquals(2, cart.getQuantity());

    }

    @Test
    @DisplayName("장바구니를 삭제해본다.")
    void deleteCart(){
        createCart();
        Cart cart = cartRepository.findAll().get(0);
        cartRepository.delete(cart);
        assertEquals(0, cartRepository.findAll().size());
    }
    
    

}