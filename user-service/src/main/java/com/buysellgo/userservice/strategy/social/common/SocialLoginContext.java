package com.buysellgo.userservice.strategy.social.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SocialLoginContext {
    private final List<SocialLoginStrategy<Map<String,Object>>> strategies;

    public SocialLoginStrategy<Map<String,Object>> getStrategy(String provider) {
        return strategies.stream()
            .filter(strategy -> strategy.support(provider))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported provider: " + provider));
    }
}
