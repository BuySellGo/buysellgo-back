package com.buysellgo.userservice.strategy.auth;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.UserRepository;
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
public class AdminAuthStrategy implements AuthStrategy<Map<String, Object>> {
    private static final long DEFAULT_HOURS = 10L;
    private static final long KEEP_LOGIN_HOURS = 168L;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> adminTemplate;

    @Override
    public AuthResult<Map<String, Object>> createJwt(AuthDto authDto) {
        User admin = userRepository.findByEmail(authDto.email())
            .orElseThrow(() -> new NoSuchElementException("Admin not found"));

        if (!passwordEncoder.matches(authDto.password(), admin.getPassword())) {
            return AuthResult.failure("Invalid password");
        }

        String accessToken = jwtTokenProvider.createToken(admin.getEmail(), Role.ADMIN.toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(admin.getEmail(), Role.ADMIN.toString());
        
        // Redis에 refreshToken 저장
        long expirationHours = authDto.keepLogin().isKeepLogin() ? KEEP_LOGIN_HOURS : DEFAULT_HOURS;
        adminTemplate.opsForValue().set(
            admin.getEmail(),
            refreshToken,
            expirationHours,
            TimeUnit.HOURS
        );

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", accessToken);
        tokenInfo.put("email", admin.getEmail());
        tokenInfo.put("adminName", admin.getUsername());
        tokenInfo.put("role", Role.ADMIN);

        return AuthResult.success(tokenInfo);
    }

    @Override
    public AuthResult<Map<String, Object>> updateJwt(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        
        Object storedToken = adminTemplate.opsForValue().get(userInfo.getEmail());
        if (storedToken == null) {
            return AuthResult.failure("Refresh token not found");
        }

        String newAccessToken = jwtTokenProvider.createToken(userInfo.getEmail(), Role.ADMIN.toString());
        
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", newAccessToken);
        tokenInfo.put("email", userInfo.getEmail());
        tokenInfo.put("role", Role.ADMIN);

        return AuthResult.success(tokenInfo);
    }

    @Override
    public AuthResult<Map<String, Object>> deleteToken(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        adminTemplate.delete(userInfo.getEmail());
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Refresh token deleted");
        return AuthResult.success(result);
    }

    @Override
    public boolean supports(Role role) {
        return Role.ADMIN.equals(role);
    }
}
