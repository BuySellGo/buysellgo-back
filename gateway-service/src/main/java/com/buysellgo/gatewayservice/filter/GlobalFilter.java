package com.buysellgo.gatewayservice.filter;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RefreshScope
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    // Filter가 빈으로 등록될 때 부모 클래스의 생성자로
    // 이미 정적(static)으로 세팅된 특정 설정값을 전달합니다.
    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    // 부모는 전달받은 설정값을 필터가 동작할 때 (apply가 호출될 때) 사용할 수 있도록
    // 매개값으로 전달해 줍니다. -> apply에서 필터의 동작 등을 제어할 수 있습니다.
    public GatewayFilter apply(GlobalFilter.Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global filter active! baseMessage = {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Global Filter called! Request URI: {}", request.getURI());
            }

            // 요청을 다음 필터로 전달 (필터 통과)
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // then 메서드 내부에 필터 체인 처리 완료 후 실행할 post-filter 로직 정의가 가능.
                if (config.isPostLogger()) {
                    log.info("Custom Post Filter active! response code: {}", response.getStatusCode());
                }
            }));
        };
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config {
        // 필터 동작을 동적으로 변경하거나 설정하기 위해 사용합니다. (선택 사항)
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
