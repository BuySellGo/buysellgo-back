package com.buysellgo.paymentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;    
import com.buysellgo.paymentservice.service.dto.ServiceResult;
import java.util.Map;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @PutMapping("/product/quantity")
    ServiceResult<Map<String, Object>> updateProductQuantity(@RequestParam Long productId, @RequestParam int quantity);

    @PutMapping("/product/quantity/restore")
    ServiceResult<Map<String, Object>> restoreProductQuantity(@RequestParam Long productId, @RequestParam int quantity);
}

