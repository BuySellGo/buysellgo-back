package com.buysellgo.userservice.strategy.auth.impl;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.auth.dto.KeepLogin;
import com.buysellgo.userservice.domain.user.LoginType;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.UserRepository;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
import com.buysellgo.userservice.strategy.auth.dto.AuthDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static com.buysellgo.userservice.util.CommonConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class AdminAuthStrategyTest {

    @InjectMocks
    private AdminAuthStrategy adminAuthStrategy;

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserRepository adminRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RedisTemplate<String, Object> adminTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    private User admin;
    private AuthDto authDto;

    @BeforeEach
    void setUp() {
        admin = User.of(
                "admin@test.com",
                "encoded_password",
                "admin",
                "010-1234-5678",
                LoginType.COMMON,
                Role.ADMIN,
                true,
                true,
                true,
                true
        );

        authDto = new AuthDto("admin@test.com", "password", Role.ADMIN, KeepLogin.ACTIVE);
    }

    @Test
    @DisplayName("관리자 JWT 생성 성공")
    void createJwt_Success() {
        // given
        when(adminTemplate.opsForValue()).thenReturn(valueOperations);
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenProvider.createToken(anyString(), anyString(), anyLong())).thenReturn("access_token");
        when(jwtTokenProvider.createRefreshToken(anyString(), anyString())).thenReturn("refresh_token");

        // when
        AuthResult<Map<String, Object>> result = adminAuthStrategy.createJwt(authDto);

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo("access_token");
        verify(valueOperations).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    @DisplayName("관리자 JWT 생성 실패 - 사용자 없음")
    void createJwt_Fail_UserNotFound() {
        // given
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when
        AuthResult<Map<String, Object>> result = adminAuthStrategy.createJwt(authDto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo(USER_NOT_FOUND.getValue());
    }

    @Test
    @DisplayName("관리자 JWT 생성 실패 - 비밀번호 불일치")
    void createJwt_Fail_PasswordNotMatched() {
        // given
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // when
        AuthResult<Map<String, Object>> result = adminAuthStrategy.createJwt(authDto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo(PASSWORD_NOT_MATCHED.getValue());
    }

    @Test
    @DisplayName("JWT 토큰 갱신 성공")
    void updateJwt_Success() {
        // given
        when(adminTemplate.opsForValue()).thenReturn(valueOperations);
        TokenUserInfo userInfo = new TokenUserInfo("admin@test.com", Role.ADMIN,1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
        when(valueOperations.get(anyString())).thenReturn("refresh_token");
        when(jwtTokenProvider.createToken(anyString(), anyString(),anyLong())).thenReturn("new_access_token");

        // when
        AuthResult<Map<String, Object>> result = adminAuthStrategy.updateJwt("old_token");

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo("new_access_token");
    }

    @Test
    @DisplayName("JWT 토큰 갱신 실패 - 사용자 없음")
    void updateJwt_Fail_UserNotFound() {
        // given
        TokenUserInfo userInfo = new TokenUserInfo("admin@test.com", Role.ADMIN,1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when
        AuthResult<Map<String, Object>> result = adminAuthStrategy.updateJwt("old_token");

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo(USER_NOT_FOUND.getValue());
    }

    @Test
    @DisplayName("JWT 토큰 갱신 실패 - Redis에 리프레시 토큰 없음")
    void updateJwt_Fail_RefreshTokenNotFound() {
        // given
        when(adminTemplate.opsForValue()).thenReturn(valueOperations);
        TokenUserInfo userInfo = new TokenUserInfo("admin@test.com", Role.ADMIN,1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
        when(valueOperations.get(anyString())).thenReturn(null);

        // when
        AuthResult<Map<String, Object>> result = adminAuthStrategy.updateJwt("old_token");

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo(TOKEN_OR_USER_NOT_FOUND.getValue());
    }

    @Test
    @DisplayName("토큰 삭제 성공")
    void deleteToken_Success() {
        // given
        when(adminTemplate.opsForValue()).thenReturn(valueOperations);
        TokenUserInfo userInfo = new TokenUserInfo("admin@test.com", Role.ADMIN,1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
        when(valueOperations.get(anyString())).thenReturn("refresh_token");

        // when
        AuthResult<Map<String, Object>> result = adminAuthStrategy.deleteToken("token");

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo(REFRESH_TOKEN_DELETED.getValue());
        verify(adminTemplate).delete(admin.getEmail());
    }

    @Test
    @DisplayName("토큰 삭제 실패 - 사용자 없음")
    void deleteToken_Fail_UserNotFound() {
        // given
        TokenUserInfo userInfo = new TokenUserInfo("admin@test.com", Role.ADMIN,1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when
        AuthResult<Map<String, Object>> result = adminAuthStrategy.deleteToken("token");

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo(USER_NOT_FOUND.getValue());
    }

    @Test
    @DisplayName("토큰 삭제 시 리프레시 토큰이 없는 경우")
    void deleteToken_WithNoRefreshToken() {
        // given
        when(adminTemplate.opsForValue()).thenReturn(valueOperations);
        TokenUserInfo userInfo = new TokenUserInfo("admin@test.com", Role.ADMIN,1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
        when(valueOperations.get(anyString())).thenReturn(null);

        // when
        AuthResult<Map<String, Object>> result = adminAuthStrategy.deleteToken("token");

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo(TOKEN_OR_USER_NOT_FOUND.getValue());
        verify(adminTemplate, never()).delete(anyString());
    }

    @Test
    @DisplayName("Role 지원 여부 확인")
    void supports() {
        // when & then
        assertThat(adminAuthStrategy.supports(Role.ADMIN)).isTrue();
        assertThat(adminAuthStrategy.supports(Role.USER)).isFalse();
    }
}