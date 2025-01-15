package com.buysellgo.helpdeskservice.common.auth;

import com.buysellgo.helpdeskservice.common.auth.TokenUserInfo;
import com.buysellgo.helpdeskservice.common.entity.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
// 클라이언트가 전송한 토큰을 검사하는 필터
public class JwtAuthFilter extends OncePerRequestFilter {

    // 필터가 해야 할 일들을 작성.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 게이트웨이가 토큰 내에 클레임을 헤더에 담아서 보내준다.
        String userEmail = request.getHeader("X-User-Email");
        String userRole = request.getHeader("X-User-Role");
        String userId = request.getHeader("X-User-ID");

        log.info("userEmail: {}, userRole: {}, userId: {}", userEmail, userRole, userId);
        log.info("request Url: {}", request.getRequestURI());


        if (userEmail != null && userRole != null) {
            // spring security에게 전달할 인가 정보 리스트를 생성. (권한 정보)
            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
            // ROLE_USER, ROLE_ADMIN (ROLE_ 접두사는 필수입니다.)
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + userRole));


            // 인증 완료 처리
            // spring security에게 인증 정보를 전달해서 전역적으로 어플리케이션 내에서
            // 인증 정보를 활용할 수 있도록 설정.
            Authentication auth = new UsernamePasswordAuthenticationToken(new TokenUserInfo(userEmail, Role.valueOf(userRole), Long.parseLong(userId)), // 컨트롤러 등에서 활용할 유저 정보
                    "", // 인증된 사용자 비밀번호: 보통 null 혹은 빈 문자열로 선언.
                    authorityList // 인가 정보 (권한)
            );

            // 시큐리티 컨테이너에 인증 정보 객체 등록
            SecurityContextHolder.getContext().setAuthentication(auth);
        }


        // 필터를 통과하는 메서드 (doFilter 안하면 필터를 통과하지 못함)
        filterChain.doFilter(request, response);
    }
}
















