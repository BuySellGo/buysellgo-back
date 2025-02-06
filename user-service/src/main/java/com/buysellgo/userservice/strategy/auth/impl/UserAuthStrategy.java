package com.buysellgo.userservice.strategy.auth.impl;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.user.Profile;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.UserRepository;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
import com.buysellgo.userservice.strategy.auth.common.AuthStrategy;
import com.buysellgo.userservice.strategy.auth.dto.AuthDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;       
import com.buysellgo.userservice.repository.ProfileRepository;

import static com.buysellgo.userservice.util.CommonConstant.*;

@Transactional
@Component
@RequiredArgsConstructor
public class UserAuthStrategy implements AuthStrategy<Map<String, Object>> {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> userTemplate;
    private final ProfileRepository profileRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Override
    public AuthResult<Map<String, Object>> createJwt(AuthDto dto, HttpServletRequest request) {
        Map<String, Object> accessStat = new HashMap<>();
        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        
        if (userOptional.isEmpty()) {
            return AuthResult.fail(USER_NOT_FOUND.getValue());
        }
        
        User user = userOptional.get();
        Profile profile = profileRepository.findByUser(user).orElseThrow();
        Map<String, Object> data = new HashMap<>();
        data.put(USER_VO.getValue(), user.toVo());
        data.put(PROFILE_VO.getValue(), profile.toVo());

        if (!passwordEncoder.matches(dto.password(), user.getPassword())){
            return AuthResult.fail(PASSWORD_NOT_MATCHED.getValue());
        }
        
        String accessToken = jwtTokenProvider.createToken(dto.email(),dto.role().toString(),user.getUserId());
        String refreshToken =jwtTokenProvider.createRefreshToken(dto.email(),dto.role().toString());

        long expirationHours = dto.keepLogin().isKeepLogin() ? KEEP_LOGIN_HOURS.getValue() : DEFAULT_HOURS.getValue();
        userTemplate.opsForValue().set(
                user.getEmail(),
                refreshToken,
                expirationHours,
                TimeUnit.HOURS
        );
        String ip = getClientIp(request);
        accessStat.put("userId", user.toVo().userId());
        accessStat.put("accessDateTime", new Timestamp(System.currentTimeMillis()));
        accessStat.put("accessIp", ip);
        kafkaTemplate.send("access-statistics",accessStat);


        return AuthResult.success(accessToken, data);
    }

    @Override
    public AuthResult<Map<String, Object>> updateJwt(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
        if (userOptional.isEmpty()) {
            return AuthResult.fail(USER_NOT_FOUND.getValue());
        }

        User user = userOptional.get();
        Map<String, Object> data = new HashMap<>();
        data.put(USER_VO.getValue(), user.toVo());

        if(ObjectUtils.anyNull(userTemplate.opsForValue().get(user.getEmail()))){
            return AuthResult.fail(TOKEN_OR_USER_NOT_FOUND.getValue());
        }
        String accessToken = jwtTokenProvider.createToken(user.getEmail(),Role.USER.toString(),user.getUserId());


        return AuthResult.success(accessToken, data);
    }

    @Override
    public AuthResult<Map<String, Object>> deleteToken(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
        if (userOptional.isEmpty()) {
            return AuthResult.fail(USER_NOT_FOUND.getValue());
        }
        User user = userOptional.get();
        Map<String, Object> data = new HashMap<>();
        data.put(USER_VO.getValue(), user.toVo());

        if(ObjectUtils.anyNull(userTemplate.opsForValue().get(user.getEmail()))){
            return AuthResult.success(TOKEN_OR_USER_NOT_FOUND.getValue(),data);
        }

        userTemplate.delete(user.getEmail());

        return AuthResult.success(REFRESH_TOKEN_DELETED.getValue(),data);
    }

    @Override
    public AuthResult<Map<String, Object>> socialSignIn(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return AuthResult.fail(USER_NOT_FOUND.getValue());
        }
        User user = userOptional.get();
        Profile profile = profileRepository.findByUser(user).orElseThrow();
        Map<String, Object> data = new HashMap<>();
        data.put(USER_VO.getValue(), user.toVo());
        data.put(PROFILE_VO.getValue(), profile.toVo());

        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString(), user.getUserId());
        data.put("access_token(개발 중에만 표시)", BEARER_PREFIX.getValue() + accessToken);
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRole().toString());

        userTemplate.opsForValue().set(user.getEmail(), refreshToken, KEEP_LOGIN_HOURS.getValue(), TimeUnit.HOURS);

        
        return AuthResult.success(accessToken, data);
    }

    @Override
    public boolean supports(Role role) {
        return Role.USER.equals(role);
    }

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 