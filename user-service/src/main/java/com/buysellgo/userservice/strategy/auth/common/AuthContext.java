package com.buysellgo.userservice.strategy.auth.common;

import com.buysellgo.userservice.common.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;



@Component
@RequiredArgsConstructor
public class AuthContext {
    private final List<AuthStrategy<?>> strategies;

    @SuppressWarnings("unchecked")
    public <T> AuthStrategy<T> getStrategy(Role role) {
        return (AuthStrategy<T>) strategies.stream()
            .filter(strategy -> strategy.supports(role))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported role: " + role));
    }
}
