package com.buysellgo.productservice.strategy.common;

import java.util.List;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.buysellgo.productservice.common.entity.Role;
import java.util.Map;
@Component
@RequiredArgsConstructor
public class ProductContext {
    private final List<ProductStrategy<Map<String, Object>>> strategies;

    public ProductStrategy<Map<String, Object>> getStrategy(Role role) {
        return strategies.stream()
            .filter(strategy -> strategy.supports(role))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported role: " + role));
    }
}
