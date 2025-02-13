package com.buysellgo.reviewservice.strategy.common;

import com.buysellgo.reviewservice.common.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class ReviewContext {
    private final List<ReviewStrategy<Map<String, Object>>> strategies;

    public ReviewStrategy<Map<String, Object>> getStrategy(Role role) {
        return strategies.stream()
            .filter(strategy -> strategy.supports(role))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported role: " + role));
    }
}

