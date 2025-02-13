package com.buysellgo.userservice.strategy.auth.impl;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
import com.buysellgo.userservice.strategy.auth.common.AuthStrategy;
import com.buysellgo.userservice.strategy.auth.dto.AuthDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.redis.core.RedisTemplate;


import static com.buysellgo.userservice.util.CommonConstant.*;

@Component
@Transactional
@RequiredArgsConstructor
public class SellerAuthStrategy implements AuthStrategy<Map<String, Object>> {
    private final JwtTokenProvider jwtTokenProvider;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> sellerTemplate;
    
    @Override
    public AuthResult<Map<String, Object>> createJwt(AuthDto dto, HttpServletRequest request) {
        Optional<Seller> sellerOptional = sellerRepository.findByEmail(dto.email());
        
        if (sellerOptional.isEmpty()) {
            return AuthResult.fail(USER_NOT_FOUND.getValue());
        }
        
        Seller seller = sellerOptional.get();
        Map<String, Object> data = new HashMap<>();
        data.put(SELLER_VO.getValue(), seller.toVo());

        if (!passwordEncoder.matches(dto.password(), seller.getPassword())){
            return AuthResult.fail(PASSWORD_NOT_MATCHED.getValue());
        }
        
        String accessToken = jwtTokenProvider.createToken(dto.email(), dto.role().toString(),seller.getSellerId());
        String refreshToken = jwtTokenProvider.createRefreshToken(dto.email(), dto.role().toString());

        long expirationHours = dto.keepLogin().isKeepLogin() ? KEEP_LOGIN_HOURS.getValue() : DEFAULT_HOURS.getValue();
        sellerTemplate.opsForValue().set(
                seller.getEmail(),
                refreshToken,
                expirationHours,
                TimeUnit.HOURS
        );

        return AuthResult.success(accessToken, data);
    }

    @Override
    public AuthResult<Map<String, Object>> updateJwt(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        Optional<Seller> sellerOptional = sellerRepository.findByEmail(userInfo.getEmail());
        if (sellerOptional.isEmpty()) {
            return AuthResult.fail(USER_NOT_FOUND.getValue());
        }

        Seller seller = sellerOptional.get();
        Map<String, Object> data = new HashMap<>();
        data.put(SELLER_VO.getValue(), seller.toVo());

        if(ObjectUtils.anyNull(sellerTemplate.opsForValue().get(seller.getEmail()))){
            return AuthResult.fail(TOKEN_OR_USER_NOT_FOUND.getValue());
        }
        String accessToken = jwtTokenProvider.createToken(seller.getEmail(), Role.SELLER.toString(),seller.getSellerId());

        return AuthResult.success(accessToken, data);
    }

    @Override
    public AuthResult<Map<String, Object>> deleteToken(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        Optional<Seller> sellerOptional = sellerRepository.findByEmail(userInfo.getEmail());
        if (sellerOptional.isEmpty()) {
            return AuthResult.fail(USER_NOT_FOUND.getValue());
        }
        Seller seller = sellerOptional.get();
        Map<String, Object> data = new HashMap<>();
        data.put(SELLER_VO.getValue(), seller.toVo());

        if(ObjectUtils.anyNull(sellerTemplate.opsForValue().get(seller.getEmail()))){
            return AuthResult.success(TOKEN_OR_USER_NOT_FOUND.getValue(), data);
        }

        sellerTemplate.delete(seller.getEmail());

        return AuthResult.success(REFRESH_TOKEN_DELETED.getValue(), data);
    }

    @Override
    public AuthResult<Map<String, Object>> socialSignIn(String email) {
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return Role.SELLER.equals(role);
    }
} 