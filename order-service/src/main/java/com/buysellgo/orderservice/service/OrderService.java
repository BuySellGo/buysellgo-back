package com.buysellgo.orderservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import com.buysellgo.orderservice.repository.OrderRepository;
import com.buysellgo.orderservice.repository.OrderGroupRepository;
import com.buysellgo.orderservice.service.dto.ServiceResult;
import com.buysellgo.orderservice.common.entity.Role;
import com.buysellgo.orderservice.controller.dto.OrderCreateReq;

import java.util.*;

import com.buysellgo.orderservice.service.dto.OrderDto;
import com.buysellgo.orderservice.entity.Order;
import com.buysellgo.orderservice.entity.OrderGroup;
import static com.buysellgo.orderservice.common.util.CommonConstant.*;

import com.buysellgo.orderservice.controller.dto.OrderStatusUpdateReq;
import org.springframework.data.domain.Sort;
import com.buysellgo.orderservice.entity.OrderStatus;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderGroupRepository orderGroupRepository;
    private final KafkaTemplate<String,Object> kafkaTemplate;


    public ServiceResult<Map<String, Object>> createOrder(long userId, OrderCreateReq req){
        Map<String, Object> data = new HashMap<>();
        try{
            OrderDto orderDto = OrderDto.from(req, userId);
            OrderGroup orderGroup = orderGroupRepository.save(OrderGroup.of(userId));
            Order order = orderRepository.save(Order.of(orderDto.getProductId(), orderDto.getProductName(), orderDto.getSellerId(), orderDto.getCompanyName(), userId, orderDto.getQuantity(), orderDto.getTotalPrice(), orderDto.getMemo(), orderDto.getPaymentMethod(), orderGroup.getGroupId()));
            data.put(ORDER_VO.getValue(), order.toVo());
            // 결재 서비스로 주문 정보 전달
            // 결재 서비스에서 결재 완료 후 주문 상태 업데이트
            return  ServiceResult.success(ORDER_CREATE_SUCCESS.getValue(), data);
        }catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(ORDER_CREATE_FAIL.getValue(), data);
        }
    }



    public ServiceResult<Map<String, Object>> getOrderList(long userId, Role role){
        Map<String, Object> data = new HashMap<>();
        try{
            if(role.equals(Role.USER)){
                List<Order> orders = orderRepository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"));
                List<Order.Vo> orderVos = orders.stream().map(Order::toVo).toList();
                data.put(ORDER_LIST_SUCCESS.getValue(), orderVos);
                return ServiceResult.success("USER"+ORDER_LIST_SUCCESS.getValue(), data);
            } else if(role.equals(Role.ADMIN)){
                List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
                List<Order.Vo> orderVos = orders.stream().map(Order::toVo).toList();
                data.put(ORDER_LIST_SUCCESS.getValue(), orderVos);
                return ServiceResult.success("ADMIN"+ORDER_LIST_SUCCESS.getValue(), data);
            } else {
                return ServiceResult.fail(ORDER_LIST_FAIL.getValue(), data);
            }
        }catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(ORDER_LIST_FAIL.getValue(), data);
        }

    }

    public ServiceResult<Map<String, Object>> updateOrderStatus( OrderStatusUpdateReq req){
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Order> order = orderRepository.findById(req.orderId());
            if(order.isEmpty()){
                return ServiceResult.fail(ORDER_NOT_FOUND.getValue(), data);
            } else {
                order.get().setStatus(req.orderStatus());
                orderRepository.save(order.get());
                data.put(ORDER_UPDATE_SUCCESS.getValue(), order.get().toVo());
                return ServiceResult.success(ORDER_UPDATE_SUCCESS.getValue(), data);
            }
        }catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            return ServiceResult.fail(ORDER_UPDATE_FAIL.getValue(), data);
        }
    }

    public ServiceResult<Map<String, Object>> updateOrderStatusSuccess(Long orderId){
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> salesStat = new HashMap<>();
        try{
            Optional<Order> order = orderRepository.findById(orderId);
            if(order.isEmpty()){
                log.info("주문 상태 업데이트 실패: 주문 없음");
                return ServiceResult.fail(ORDER_NOT_FOUND.getValue(), data);
            }
            order.get().setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order.get());
            data.put(ORDER_UPDATE_SUCCESS.getValue(), order.get().toVo());
            log.info("주문 상태 업데이트 완료: {}", order.get().toVo());

            salesStat.put("sellerId", order.get().toVo().sellerId());
            salesStat.put("createdAt", order.get().toVo().createdAt());
            salesStat.put("salesAmount",order.get().toVo().totalPrice());
            kafkaTemplate.send("sales-statistics", salesStat);

            return ServiceResult.success(ORDER_UPDATE_SUCCESS.getValue(), data);
        }catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            log.info("주문 상태 업데이트 실패: {}", e.getMessage());
            return ServiceResult.fail(ORDER_UPDATE_FAIL.getValue(), data);
        }
    }


    public ServiceResult<Map<String, Object>> updateOrderStatusCancelled(Long orderId){
        Map<String, Object> data = new HashMap<>();
        try{
            Optional<Order> order = orderRepository.findById(orderId);
            if(order.isEmpty()){
                log.info("주문 상태 업데이트 실패: 주문 없음");
                return ServiceResult.fail(ORDER_NOT_FOUND.getValue(), data);
            }
            order.get().setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order.get());
            data.put(ORDER_UPDATE_SUCCESS.getValue(), order.get().toVo());

            log.info("주문 상태 업데이트 완료: {}", order.get().toVo());
            return ServiceResult.success(ORDER_UPDATE_SUCCESS.getValue(), data);

        }catch(Exception e){
            data.put(FAILURE.getValue(), e.getMessage());
            log.info("주문 상태 업데이트 실패: {}", e.getMessage());
            return ServiceResult.fail(ORDER_UPDATE_FAIL.getValue(), data);
        }


    }


}
