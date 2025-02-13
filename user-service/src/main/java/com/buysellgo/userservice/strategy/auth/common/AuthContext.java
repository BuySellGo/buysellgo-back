package com.buysellgo.userservice.strategy.auth.common;

import com.buysellgo.userservice.common.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;



@Component
@RequiredArgsConstructor
public class AuthContext {
    private final List<AuthStrategy<Map<String, Object>>> strategies;

    public AuthStrategy<Map<String, Object>> getStrategy(Role role) {
        return strategies.stream()
            .filter(strategy -> strategy.supports(role))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported role: " + role));
    }
}
