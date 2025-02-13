package com.buysellgo.userservice.strategy.auth.impl;


import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.admin.Admin;
import com.buysellgo.userservice.repository.AdminRepository;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
import com.buysellgo.userservice.strategy.auth.common.AuthStrategy;
import com.buysellgo.userservice.strategy.auth.dto.AuthDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.buysellgo.userservice.util.CommonConstant.*;

@Component
@Transactional
@RequiredArgsConstructor
public class AdminAuthStrategy implements AuthStrategy<Map<String, Object>> {
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> adminTemplate;
    
    @Override
    public AuthResult<Map<String, Object>> createJwt(AuthDto dto, HttpServletRequest request) {
        Optional<Admin> adminOptional = adminRepository.findByEmail(dto.email());
        
        if (adminOptional.isEmpty()) {
            return AuthResult.fail(USER_NOT_FOUND.getValue());
        }
        
        Admin admin = adminOptional.get();
        Map<String, Object> data = new HashMap<>();
        data.put(ADMIN_VO.getValue(), admin.toVo());

        if (!passwordEncoder.matches(dto.password(), admin.getPassword())){
            return AuthResult.fail(PASSWORD_NOT_MATCHED.getValue());
        }

        String accessToken = jwtTokenProvider.createToken(dto.email(), dto.role().toString(),admin.getAdminId());
        String refreshToken = jwtTokenProvider.createRefreshToken(dto.email(), dto.role().toString());

        long expirationHours = dto.keepLogin().isKeepLogin() ? KEEP_LOGIN_HOURS.getValue() : DEFAULT_HOURS.getValue();
        adminTemplate.opsForValue().set(
                admin.getEmail(),
                refreshToken,
                expirationHours,
                TimeUnit.HOURS
        );

        return AuthResult.success(accessToken, data);
    }

    @Override
    public AuthResult<Map<String, Object>> updateJwt(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        Optional<Admin> adminOptional = adminRepository.findByEmail(userInfo.getEmail());
        if (adminOptional.isEmpty()) {
            return AuthResult.fail(USER_NOT_FOUND.getValue());
        }

        Admin admin = adminOptional.get();
        Map<String, Object> data = new HashMap<>();
        data.put(ADMIN_VO.getValue(), admin.toVo());

        if(ObjectUtils.anyNull(adminTemplate.opsForValue().get(admin.getEmail()))){
            return AuthResult.fail(TOKEN_OR_USER_NOT_FOUND.getValue());
        }
        String accessToken = jwtTokenProvider.createToken(admin.getEmail(), admin.getRole().toString(),admin.getAdminId());

        return AuthResult.success(accessToken, data);
    }

    @Override
    public AuthResult<Map<String, Object>> deleteToken(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        Optional<Admin> adminOptional = adminRepository.findByEmail(userInfo.getEmail());
        if (adminOptional.isEmpty()) {
            return AuthResult.fail(USER_NOT_FOUND.getValue());
        }
        Admin admin = adminOptional.get();
        Map<String, Object> data = new HashMap<>();
        data.put(ADMIN_VO.getValue(), admin.toVo());

        if(ObjectUtils.anyNull(adminTemplate.opsForValue().get(admin.getEmail()))){
            return AuthResult.success(TOKEN_OR_USER_NOT_FOUND.getValue(), data);
        }

        adminTemplate.delete(admin.getEmail());

        return AuthResult.success(REFRESH_TOKEN_DELETED.getValue(), data);
    }

    @Override
    public AuthResult<Map<String, Object>> socialSignIn(String email) {
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return Role.ADMIN.equals(role);
    }
} 