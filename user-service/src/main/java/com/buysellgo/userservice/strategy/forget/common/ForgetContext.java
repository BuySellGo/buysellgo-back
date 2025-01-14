package com.buysellgo.userservice.strategy.forget.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.buysellgo.userservice.common.entity.Role;

import lombok.RequiredArgsConstructor;

@Component  
@RequiredArgsConstructor
public class ForgetContext {
    private final List<ForgetStrategy<Map<String, Object>>> strategies;

    public ForgetStrategy<Map<String, Object>> getStrategy(Role role) {
        return strategies.stream()
            .filter(strategy -> strategy.supports(role))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported role: " + role));
    }
}
