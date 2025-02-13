package com.buysellgo.qnaservice.strategy.common;

import java.util.List;
import org.springframework.stereotype.Component;
import com.buysellgo.qnaservice.common.entity.Role;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QnaContext {
    private final List<QnaStrategy<Map<String, Object>>> strategies;

    public QnaStrategy<Map<String, Object>> getStrategy(Role role) {
        return strategies.stream()
            .filter(strategy -> strategy.supports(role))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported role: " + role));
    }
}
