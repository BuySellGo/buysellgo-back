package com.buysellgo.userservice.strategy.info.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.buysellgo.userservice.common.entity.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InfoContext {
    private final List<InfoStrategy<Map<String, Object>>> strategies;

    public InfoStrategy<Map<String, Object>> getStrategy(Role role) {
        return strategies.stream()
            .filter(strategy -> strategy.supports(role))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported role: " + role));
    }
}
