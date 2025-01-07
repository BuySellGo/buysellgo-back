package com.buysellgo.userservice.service;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.repository.UserRepository;
import com.buysellgo.userservice.service.dto.AuthDto;
import com.buysellgo.userservice.strategy.auth.common.AuthContext;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
import com.buysellgo.userservice.strategy.auth.common.AuthStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String USERNAME = "username";
    private static final String COMPANY_NAME = "companyName";
    private static final String FAILURE = "failure";
    private static final long DEFAULT_HOURS = 10L;  // 기본 10시간
    private static final long KEEP_LOGIN_HOURS = 168L;  // 로그인 유지 7일 (7 * 24 = 168시간)
    private static final String EMAIL = "email";
    private static final String ROLE = "role";
    private static final String SUCCESS = "success";
    private static final String REFRESH_TOKEN_DELETED="refresh token deleted";
    
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> userTemplate;
    private final RedisTemplate<String, Object> sellerTemplate;
    private final RedisTemplate<String, Object> adminTemplate;
    private final AuthContext authContext;

    public AuthResult<Map<String, Object>> createJwt(JwtCreateReq req) {
        AuthDto authDto = AuthDto.from(req);
        AuthStrategy<Map<String, Object>> strategy = authContext.getStrategy(authDto.role());
        return strategy.createJwt(authDto);
    }

    public AuthResult<Map<String, Object>> updateJwt(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        AuthStrategy<Map<String, Object>> strategy = authContext.getStrategy(userInfo.getRole());
        return strategy.updateJwt(token);
    }

    public AuthResult<Map<String, Object>> deleteToken(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        AuthStrategy<Map<String, Object>> strategy = authContext.getStrategy(userInfo.getRole());
        return strategy.deleteToken(token);
    }
}
