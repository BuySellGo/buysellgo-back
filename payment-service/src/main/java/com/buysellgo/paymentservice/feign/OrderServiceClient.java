package com.buysellgo.paymentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.buysellgo.paymentservice.service.dto.ServiceResult;
import java.util.Map;


@FeignClient(name = "order-service")
public interface OrderServiceClient {
    @PutMapping("/order/status/success")
    ServiceResult<Map<String, Object>> updateOrderStatusSuccess(@RequestParam Long orderId);

    @PutMapping("/order/status/cancelled")
    ServiceResult<Map<String, Object>> updateOrderStatusCancelled(@RequestParam Long orderId);

}
