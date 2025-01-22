package com.buysellgo.chatservice.common.configs;

import com.buysellgo.chatservice.common.auth.JwtAuthFilter;
import com.buysellgo.chatservice.common.dto.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public static final String ALLOWED_IP_ADDRESS = "127.0.0.1";
    public static final String SUBNET = "/32";
    public static final IpAddressMatcher IP_ADDRESS_MATCHER
            = new IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        // CORS 설정
        http.cors(cors -> cors.configurationSource(request -> {
            var config = new org.springframework.web.cors.CorsConfiguration();
            config.addAllowedOriginPattern("*"); // 모든 도메인 허용
            config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
            config.addAllowedHeader("*"); // 모든 헤더 허용
            config.setAllowCredentials(true); // 자격 증명 허용
            return config;
        }));

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/ws/**", "/app/**", "/topic/**", "/queue/**", "/topics/**", "/chat/**",
                        "/swagger-ui/**", "/v3/api-docs/**",
                        "/user-service/v3/api-docs/**",
                        "/user-service/swagger-ui/**",
                        "/user-service/swagger-ui.html",
                        "/user-service/swagger-resources/**",
                        "/user-service/webjars/**",
                        "/user-service/swagger-ui-custom.html",
                        "/user-service/api/v1/**"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/jwt").permitAll() // 로그인
                .anyRequest().authenticated()
        );

        // JWT 필터 추가
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}















