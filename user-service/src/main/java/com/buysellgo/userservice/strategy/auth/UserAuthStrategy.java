package com.buysellgo.userservice.strategy.auth;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.UserRepository;
import com.buysellgo.userservice.service.dto.AuthDto;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
import com.buysellgo.userservice.strategy.auth.common.AuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class UserAuthStrategy implements AuthStrategy<Map<String, Object>> {
    private static final long DEFAULT_HOURS = 10L;
    private static final long KEEP_LOGIN_HOURS = 168L;
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> userTemplate;

    @Override
    public AuthResult<Map<String, Object>> createJwt(AuthDto authDto) {
        User user = userRepository.findByEmail(authDto.email())
            .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!passwordEncoder.matches(authDto.password(), user.getPassword())) {
            return AuthResult.failure("Invalid password");
        }

        String accessToken = jwtTokenProvider.createToken(user.getEmail(), Role.USER.toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), Role.USER.toString());
        
        // Redis에 refreshToken 저장
        long expirationHours = authDto.keepLogin().isKeepLogin() ? KEEP_LOGIN_HOURS : DEFAULT_HOURS;
        userTemplate.opsForValue().set(
            user.getEmail(),
            refreshToken,
            expirationHours,
            TimeUnit.HOURS
        );

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", accessToken);
        tokenInfo.put("email", user.getEmail());
        tokenInfo.put("username", user.getUsername());
        tokenInfo.put("role", Role.USER);

        return AuthResult.success(tokenInfo);
    }

    @Override
    public AuthResult<Map<String, Object>> updateJwt(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        
        Object storedToken = userTemplate.opsForValue().get(userInfo.getEmail());
        if (storedToken == null) {
            return AuthResult.failure("Refresh token not found");
        }

        String newAccessToken = jwtTokenProvider.createToken(userInfo.getEmail(), Role.USER.toString());
        
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", newAccessToken);
        tokenInfo.put("email", userInfo.getEmail());
        tokenInfo.put("role", Role.USER);

        return AuthResult.success(tokenInfo);
    }

    @Override
    public AuthResult<Map<String, Object>> deleteToken(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        userTemplate.delete(userInfo.getEmail());
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Refresh token deleted");
        return AuthResult.success(result);
    }

    @Override
    public boolean supports(Role role) {
        return Role.USER.equals(role);
    }
}
