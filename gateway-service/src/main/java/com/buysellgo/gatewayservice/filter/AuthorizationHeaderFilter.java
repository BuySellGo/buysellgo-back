package com.buysellgo.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
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

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class AuthorizationHeaderFilter
        extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private SecretKey getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // URL과 HTTP 메서드를 함께 저장하는 내부 클래스
    private static class RoutePattern {
        String path;
        String method;  // null이면 모든 메서드 허용

        RoutePattern(String path) {
            this.path = path;
            this.method = null;
        }

        RoutePattern(String path, String method) {
            this.path = path;
            this.method = method;
        }
    }

    private final List<RoutePattern> allowPatterns = Arrays.asList(
            // 모든 메서드 허용
            // user-service
            new RoutePattern("/sign/user"),
            new RoutePattern("/sign/seller"),
            new RoutePattern("/sign/admin"),
            new RoutePattern("/sign/duplicate"),
            new RoutePattern("/sign/social"),
            new RoutePattern("/sign/kakao"),
            new RoutePattern("/sign/naver"),
            new RoutePattern("/sign/google"),
            new RoutePattern("/forget/email"),
            new RoutePattern("/forget/password"),
            // review-service
            new RoutePattern("/review/list/guest"),
            // qna-service
            new RoutePattern("/qna/list/guest"),
            // HTTP 메서드별 허용
            new RoutePattern("/auth/jwt", "POST"),   // 로그인

            // Swagger 관련
            new RoutePattern("/user-service/v3/api-docs/**"),
            new RoutePattern("/v3/api-docs/**"),
            new RoutePattern("/swagger-ui/**"),
            new RoutePattern("/swagger-resources/**"),
            new RoutePattern("/webjars/**"),
            new RoutePattern("/swagger-ui.html"),
            new RoutePattern("/swagger-ui-custom.html"),

            // For Swagger test with API Gateway
            new RoutePattern("/api/v1/hello-user-service"),
            new RoutePattern("/api/v1/hello-helpdesk-service"),
            new RoutePattern("/api/v1/hello-promotion-service"),
            new RoutePattern("/api/v1/hello-statistics-service"),
            new RoutePattern("/api/v1/hello-delivery-service")

    );

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethod().name();
            AntPathMatcher antPathMatcher = new AntPathMatcher();

            // WebSocket 요청인지 확인(웹소켓은 http가 아니라 websocket이라서 추가)
            if (exchange.getRequest().getHeaders().containsKey("Upgrade") &&
                "websocket".equalsIgnoreCase(exchange.getRequest().getHeaders().getFirst("Upgrade"))) {
                return chain.filter(exchange);
            }

            // 허용된 패턴인지 확인
            boolean isAllowed = allowPatterns.stream()
                    .anyMatch(pattern ->
                            antPathMatcher.match(pattern.path, path) &&
                                    (pattern.method == null || pattern.method.equals(method))
                    );

            if (isAllowed) {
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
            Optional<Claims> claimsOptional = validateJwt(token);
            if (claimsOptional.isEmpty()) {
                return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }

            // 사용자 정보를 클레임에서 꺼내서 헤더에 담자.
            Claims claims = claimsOptional.get();
            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("X-User-Email", claims.getSubject())
                    .header("X-User-Role", claims.get("role", String.class))
                    .header("X-User-ID", String.valueOf(claims.get("id", Long.class)))
                    .build();

            // 새롭게 만든(토큰 정보를 헤더에 담은) request를 exchange에 갈아끼워서 보내자.
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    private Optional<Claims> validateJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return Optional.empty();
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

    @Getter
    @Setter
    public static class Config {
        private String secretKey;  // JWT 시크릿 키
        private List<String> excludePaths; // 인증 제외 경로

        // 기본 생성자
        public Config() {
        }

        // 파라미터가 있는 생성자
        public Config(String secretKey, List<String> excludePaths) {
            this.secretKey = secretKey;
            this.excludePaths = excludePaths;
        }
    }

}
