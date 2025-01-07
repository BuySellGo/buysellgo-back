package com.buysellgo.userservice.strategy.auth;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.service.dto.AuthDto;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
import com.buysellgo.userservice.strategy.auth.common.AuthStrategy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class SellerAuthStrategy implements AuthStrategy<Map<String, Object>> {
    private static final long DEFAULT_HOURS = 10L;
    private static final long KEEP_LOGIN_HOURS = 168L;
    
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> sellerTemplate;

    @Override
    public AuthResult<Map<String, Object>> createJwt(AuthDto authDto) {
        Seller seller = sellerRepository.findByEmail(authDto.email())
            .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        if (!passwordEncoder.matches(authDto.password(), seller.getPassword())) {
            return AuthResult.failure("Invalid password");
        }

        String accessToken = jwtTokenProvider.createToken(seller.getEmail(), Role.SELLER.toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(seller.getEmail(), Role.SELLER.toString());
        
        // Redis에 refreshToken 저장
        long expirationHours = authDto.keepLogin().isKeepLogin() ? KEEP_LOGIN_HOURS : DEFAULT_HOURS;
        sellerTemplate.opsForValue().set(
            seller.getEmail(),
            refreshToken,
            expirationHours,
            TimeUnit.HOURS
        );

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", accessToken);
        tokenInfo.put("email", seller.getEmail());
        tokenInfo.put("companyName", seller.getCompanyName());
        tokenInfo.put("role", Role.SELLER);

        return AuthResult.success(tokenInfo);
    }

    @Override
    public AuthResult<Map<String, Object>> updateJwt(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        
        Object storedToken = sellerTemplate.opsForValue().get(userInfo.getEmail());
        if (storedToken == null) {
            return AuthResult.failure("Refresh token not found");
        }

        String newAccessToken = jwtTokenProvider.createToken(userInfo.getEmail(), Role.SELLER.toString());
        
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", newAccessToken);
        tokenInfo.put("email", userInfo.getEmail());
        tokenInfo.put("role", Role.SELLER);

        return AuthResult.success(tokenInfo);
    }

    @Override
    public AuthResult<Map<String, Object>> deleteToken(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        sellerTemplate.delete(userInfo.getEmail());
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Refresh token deleted");
        return AuthResult.success(result);
    }

    @Override
    public boolean supports(Role role) {
        return Role.SELLER.equals(role);
    }
}
