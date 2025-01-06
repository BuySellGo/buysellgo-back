package com.buysellgo.userservice.service;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.controller.dto.KeepLogin;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    public Map<String, Object> createJwt(JwtCreateReq req) {
        Map<String, Object> map = new HashMap<>();
        long expirationHours = req.keepLogin() == KeepLogin.ACTIVE ? KEEP_LOGIN_HOURS : DEFAULT_HOURS;

        if(req.role().equals(Role.USER)) {
            User user = userRepository.findByEmail(req.email()).orElseThrow();
            if(passwordEncoder.matches(req.password(), user.getPassword())){
                String accessToken = jwtTokenProvider.createToken(req.email(), req.role().toString());
                String refreshToken = jwtTokenProvider.createRefreshToken(req.email(), req.role().toString());
                userTemplate.opsForValue().set(user.getEmail(), refreshToken, expirationHours, TimeUnit.HOURS);
                map.put(ACCESS_TOKEN, accessToken);
                map.put(USERNAME, user.getUsername());
                return map;
            }
        } else if (req.role().equals(Role.SELLER)) {
            Seller seller = sellerRepository.findByEmail(req.email()).orElseThrow();
            if(passwordEncoder.matches(req.password(), seller.getPassword())){
                String accessToken = jwtTokenProvider.createToken(req.email(), req.role().toString());
                String refreshToken = jwtTokenProvider.createRefreshToken(req.email(), req.role().toString());
                sellerTemplate.opsForValue().set(seller.getEmail(), refreshToken, expirationHours, TimeUnit.HOURS);
                map.put(ACCESS_TOKEN, accessToken);
                map.put(COMPANY_NAME, seller.getCompanyName());
                return map;
            }
        } else if (req.role().equals(Role.ADMIN)) {
            User user = userRepository.findByEmail(req.email()).orElseThrow();
            if(passwordEncoder.matches(req.password(), user.getPassword())){
                String accessToken = jwtTokenProvider.createToken(req.email(), req.role().toString());
                String refreshToken = jwtTokenProvider.createRefreshToken(req.email(), req.role().toString());
                adminTemplate.opsForValue().set(user.getEmail(), refreshToken, expirationHours, TimeUnit.HOURS);
                map.put(ACCESS_TOKEN, accessToken);
                map.put(USERNAME, user.getUsername());
                return map;
            }
        }
        map.put(FAILURE, "login fail");
        return map;
    }

    public Map<String, Object> updateJwt(String accessToken) {
        Map<String, Object> map = new HashMap<>();
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(accessToken);
        if(userInfo.getRole().equals(Role.USER)) {
            User user = userRepository.findByEmail(userInfo.getEmail()).orElseThrow();
            Object object = userTemplate.opsForValue().get(user.getEmail());
            refreshTokenCheck(object, map);
            String newAccessToken = jwtTokenProvider.createToken(userInfo.getEmail(), userInfo.getRole().toString());
            map.put(ACCESS_TOKEN, newAccessToken);
            map.put(USERNAME, user.getUsername());
            map.put(EMAIL,userInfo.getEmail());
            map.put(ROLE,userInfo.getRole());
            return map;
        } else if (userInfo.getRole().equals(Role.SELLER)) {
            Seller seller = sellerRepository.findByEmail(userInfo.getEmail()).orElseThrow();
            Object object = sellerTemplate.opsForValue().get(userInfo.getEmail());
            refreshTokenCheck(object, map);
            String newAccessToken = jwtTokenProvider.createToken(userInfo.getEmail(), userInfo.getRole().toString());
            map.put(ACCESS_TOKEN, newAccessToken);
            map.put(COMPANY_NAME, seller.getCompanyName());
            map.put(EMAIL,userInfo.getEmail());
            map.put(ROLE,userInfo.getRole());
            return map;
        } else if (userInfo.getRole().equals(Role.ADMIN)) {
            User user = userRepository.findByEmail(userInfo.getEmail()).orElseThrow();
            Object object = userTemplate.opsForValue().get(userInfo.getEmail());
            refreshTokenCheck(object, map);
            String newAccessToken = jwtTokenProvider.createToken(userInfo.getEmail(), userInfo.getRole().toString());
            map.put(ACCESS_TOKEN, newAccessToken);
            map.put(USERNAME, user.getUsername());
            map.put(EMAIL,userInfo.getEmail());
            map.put(ROLE,userInfo.getRole());
            return map;
        }
        map.put(FAILURE, "User not found");
        return map;
    }

    private static void refreshTokenCheck(Object object, Map<String, Object> map) {
        if (ObjectUtils.anyNull(object)) {
            map.put(FAILURE, "RefreshToken not found");
        }
    }

    public Map<String, Object> deleteToken(String token) {
        Map<String, Object> map = new HashMap<>();
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        if(userInfo.getRole().equals(Role.USER)) {
            userTemplate.opsForValue().getOperations().delete(userInfo.getEmail());
            map.put(SUCCESS, REFRESH_TOKEN_DELETED);
            return map;
        } else if(userInfo.getRole().equals(Role.SELLER)){
            sellerTemplate.opsForValue().getOperations().delete(userInfo.getEmail());
            map.put(SUCCESS, REFRESH_TOKEN_DELETED);
            return map;
        } else if(userInfo.getRole().equals(Role.ADMIN)){
            adminTemplate.opsForValue().getOperations().delete(userInfo.getEmail());
            map.put(SUCCESS, REFRESH_TOKEN_DELETED);
            return map;
        }
        map.put(FAILURE, "RefreshToken not found");
        return map;
    }
}
