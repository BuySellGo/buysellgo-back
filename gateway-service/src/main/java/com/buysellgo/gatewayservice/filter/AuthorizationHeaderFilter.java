package com.buysellgo.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AuthorizationHeaderFilter
        extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final List<String> allowUrl = Arrays.asList(
            "/sign-up", "/sign-in",
            "/api/v1/hello-user-service",
            "/api/v1/hello-helpdesk-service",
            "/api/v1/hello-promotion-service",
            "/api/v1/hello-statistics-service",
            "/api/v1/hello-delivery-service",
            "/v3/api-docs",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/swagger-ui-custom.html"
    );

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            AntPathMatcher antPathMatcher = new AntPathMatcher();

            // 허용 url 리스트를 순회하면서 지금 들어온 요청 url과 하나라도 일치하면 true 리턴
            boolean isAllowed
                    = allowUrl.stream().anyMatch(url -> antPathMatcher.match(url, path));
            if (isAllowed) {
                // 허용 url이 맞다면 그냥 통과~
                return chain.filter(exchange);
            }

            // 토큰이 필요한 요청은 Header에 Authorization이라는 이름으로 Bearer ~~~가 전달됨.
            String authorizationHeader
                    = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                // 토큰이 존재하지 않거나, Bearer로 시작하지 않는다면
                return onError(exchange, "Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
            }

            // Bearer 떼기
            String token = authorizationHeader.replace("Bearer ", "");

            // JWT 토큰 유효성 검증 및 클레임 얻어내기
            Claims claims = validateJwt(token);
            if (claims == null) {
                return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }

            // 사용자 정보를 클레임에서 꺼내서 헤더에 담자.
            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("X-User-Email", claims.getSubject())
                    .header("X-User-Role", claims.get("role", String.class))
                    .build();

            // 새롭게 만든(토큰 정보를 헤더에 담은) request를 exchange에 갈아끼워서 보내자.
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    private Claims validateJwt(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return null;
        }
    }

    // Spring Webflux에서 사용하는 타입 Mono, Flux
    // Mono: 단일 값 또는 완료 신호 등을 처리
    // Flux: 여러 데이터 블록, 스트림을 처리
    // request, response를 바로 사용하지 않고 Mono, Flux를 사용하는 이유는 게이트웨이 서버가
    // 우리가 기존에 사용하던 톰캣 서버가 아닌 비동기 I/O 모델 (Netty)를 사용하기 때문.
    private Mono<Void> onError(ServerWebExchange exchange, String msg, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(msg);

        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
    }

    public static class Config {
        // 기타 설정값 가져오는 로직 작성 가능.
    }

}
