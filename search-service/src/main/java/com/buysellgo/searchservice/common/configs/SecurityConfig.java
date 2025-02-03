package com.buysellgo.searchservice.common.configs;

import com.buysellgo.searchservice.common.auth.JwtAuthFilter;
import com.buysellgo.searchservice.common.dto.CustomAuthenticationEntryPoint;
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
@EnableMethodSecurity // 권한 검사를 컨트롤러의 메서드에서 전역적으로 수행하기 위한 설정.
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // 로컬에서의 내 아이피 주소 및 localhost의 서브넷을 상수로 지정.
    // 서브넷: ip주소 공간을 작은 네트워크로 분할하는 기술.
    public static final String ALLOWED_IP_ADDRESS = "127.0.0.1";
    public static final String SUBNET = "/32"; // 서브넷 기본값

    // 들어오는 요청이 해당 ip와 서브넷을 만족하는 요청인지를 구분해주는 객체.
    public static final IpAddressMatcher IP_ADDRESS_MATCHER
            = new IpAddressMatcher(ALLOWED_IP_ADDRESS +  SUBNET);

    // 시큐리티 기본 설정 (권한 처리, 초기 로그인 화면 없애기 등등...)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
//        http.cors(Customizer.withDefaults()); // 직접 커스텀한 CORS 설정을 적용하겠다.
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
//                    .requestMatchers("/user/list").hasAnyRole("ADMIN")
                .requestMatchers(
                        "/swagger-ui/**","/v3/api-docs/**",
                        "/search-service/v3/api-docs"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/jwt").permitAll()    // 로그인
//                    .requestMatchers("/**").access(
//                            new WebExpressionAuthorizationManager("hasIpAddress('localhost') or hasIpAddress('::1') or hasIpAddress('127.0.0.1')  or hasIpAddress('172.30.67.125')")
//                    )
                .anyRequest().authenticated())
                // 커스텀 필터를 등록.
                // 시큐리티에서 기본으로 인증, 인가 처리를 해 주는 UsernamePasswordAuthenticationFilter 전에 내 필터 add
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



}














