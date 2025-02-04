package com.buysellgo.orderservice.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.buysellgo.orderservice.repository.CartRepository;
import com.buysellgo.orderservice.repository.OrderRepository;
import com.buysellgo.orderservice.service.dto.ServiceResult;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import com.buysellgo.orderservice.controller.dto.CartCreateReq;
import com.buysellgo.orderservice.service.dto.CartDto;
import com.buysellgo.orderservice.entity.Cart;
import static com.buysellgo.orderservice.common.util.CommonConstant.*;
import java.util.Optional;
import com.buysellgo.orderservice.controller.dto.CartOrderReq;
import com.buysellgo.orderservice.service.dto.OrderDto;
import com.buysellgo.orderservice.entity.Order;
import com.buysellgo.orderservice.entity.OrderGroup;
import com.buysellgo.orderservice.repository.OrderGroupRepository;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderGroupRepository orderGroupRepository;

    public ServiceResult<Map<String, Object>> addCartItem(CartCreateReq req, long userId){
        Map<String, Object> data = new HashMap<>();
        try{
            CartDto cartDto = CartDto.from(req, userId);
            Cart cart = cartRepository.save(Cart.of(cartDto.getUserId(), cartDto.getProductId(), cartDto.getProductName(), cartDto.getSellerId(), cartDto.getCompanyName(), cartDto.getQuantity(), cartDto.getPrice()));
            data.put(CART_VO.getValue(), cart.toVo());
            return ServiceResult.success(CART_ADD_SUCCESS.getValue(),data);

        }catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(CART_ADD_FAIL.getValue(),data);
        }
    }



    public ServiceResult<Map<String, Object>> getCartList(long userId){
        Map<String, Object> data = new HashMap<>();
        try{
            List<Cart> cartList = cartRepository.findAllByUserId(userId);
            if(cartList.isEmpty()){
                data.put(FAILURE.getValue(), CART_NOT_FOUND.getValue());
                return ServiceResult.fail(CART_LIST_FAIL.getValue(),data);
            }
            data.put(CART_VO.getValue(), cartList.stream().map(Cart::toVo).toList());
            return ServiceResult.success(CART_LIST_SUCCESS.getValue(),data);

        }catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(CART_LIST_FAIL.getValue(),data);
        }
    }


    public ServiceResult<Map<String, Object>> updateCartItem(long cartId, int quantity, long userId){
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Cart> cartOptional = cartRepository.findById(cartId);
            if(cartOptional.isEmpty()){
                data.put(FAILURE.getValue(), CART_NOT_FOUND.getValue());
                return ServiceResult.fail(CART_UPDATE_FAIL.getValue(),data);
            }
            if(cartOptional.get().getUserId() != userId){
                data.put(FAILURE.getValue(), CART_NOT_AUTHORIZED.getValue());
                return ServiceResult.fail(CART_UPDATE_FAIL.getValue(),data);
            }
            Cart cart = cartOptional.get();
            cart.setQuantity(quantity);
            cartRepository.save(cart);
            data.put(CART_VO.getValue(), cart.toVo());
            return ServiceResult.success(CART_UPDATE_SUCCESS.getValue(),data);

        }catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(CART_UPDATE_FAIL.getValue(),data);
        }


    }


    public ServiceResult<Map<String, Object>> orderCartItem(CartOrderReq req, long userId){
        Map<String, Object> data = new HashMap<>();
        try{
            List<Cart> cartList = cartRepository.findAllById(req.cartIds());
            if(cartList.isEmpty()){
                data.put(FAILURE.getValue(), CART_NOT_FOUND.getValue());
                return ServiceResult.fail(CART_ORDER_FAIL.getValue(),data);
            }
            if(cartList.stream().anyMatch(cart -> cart.getUserId() != userId)){
                data.put(FAILURE.getValue(), CART_NOT_AUTHORIZED.getValue());
                return ServiceResult.fail(CART_ORDER_FAIL.getValue(),data);
            }
            OrderGroup orderGroup = orderGroupRepository.save(OrderGroup.of(userId));
            List<Order.Vo> orderVoList = new ArrayList<>();

            cartList.forEach(cart -> {
                OrderDto orderDto = OrderDto.from(cart, req.memo(), req.paymentMethod());
                Order order = orderRepository.save(Order.of(orderDto.getProductId(), orderDto.getProductName(), orderDto.getSellerId(), orderDto.getCompanyName(), orderDto.getUserId(), orderDto.getQuantity(), orderDto.getTotalPrice(), orderDto.getMemo(), orderDto.getPaymentMethod(), orderGroup.getGroupId()));
                orderVoList.add(order.toVo());
            });
            data.put(ORDER_VO.getValue(), orderVoList);
            cartRepository.deleteAll(cartList);
            // 주문 서비스로 주문 정보 전송
            return ServiceResult.success(CART_ORDER_SUCCESS.getValue(),data);

        } catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(CART_ORDER_FAIL.getValue(),data);
        }

    }


    public ServiceResult<Map<String, Object>> deleteCartItem(long cartId, long userId){
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Cart> cartOptional = cartRepository.findById(cartId);
            if(cartOptional.isEmpty()){
                data.put(FAILURE.getValue(), CART_NOT_FOUND.getValue());
                return ServiceResult.fail(CART_DELETE_FAIL.getValue(),data);
            }
            if(cartOptional.get().getUserId() != userId){
                data.put(FAILURE.getValue(), CART_NOT_AUTHORIZED.getValue());
                return ServiceResult.fail(CART_DELETE_FAIL.getValue(),data);
            }
            Cart cart = cartOptional.get();
            cartRepository.delete(cart);
            data.put(CART_VO.getValue(), cart.toVo());
            return ServiceResult.success(CART_DELETE_SUCCESS.getValue(),data);

        }catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(CART_DELETE_FAIL.getValue(),data);
        }
    }
}
