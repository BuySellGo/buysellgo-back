package com.buysellgo.userservice.strategy.auth;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.dto.KeepLogin;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.UserRepository;
import com.buysellgo.userservice.service.dto.AuthDto;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthStrategyTest {
    @InjectMocks
    private UserAuthStrategy userAuthStrategy;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisTemplate<String, Object> userTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private User user;
    private AuthDto authDto;
    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "password123!";
    private static final String TEST_USERNAME = "testUser";

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        authDto = new AuthDto(
            TEST_EMAIL,
            TEST_PASSWORD,
            Role.USER,
            KeepLogin.INACTIVE
        );
    }

    @Test
    @DisplayName("JWT 생성 성공")
    void createJwt_Success() {
        // given
        when(userTemplate.opsForValue()).thenReturn(valueOperations);
        when(user.getEmail()).thenReturn(TEST_EMAIL);
        when(user.getUsername()).thenReturn(TEST_USERNAME);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtTokenProvider.createToken(TEST_EMAIL, Role.USER.toString())).thenReturn("accessToken");
        when(jwtTokenProvider.createRefreshToken(TEST_EMAIL, Role.USER.toString())).thenReturn("refreshToken");

        // when
        AuthResult<Map<String, Object>> result = userAuthStrategy.createJwt(authDto);

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.data())
            .containsEntry("accessToken", "accessToken")
            .containsEntry("email", TEST_EMAIL)
            .containsEntry("username", TEST_USERNAME)
            .containsEntry("role", Role.USER);

        verify(valueOperations).set(
            TEST_EMAIL,
            "refreshToken",
            168L,
            TimeUnit.HOURS
        );
    }

    @Test
    @DisplayName("JWT 생성 실패 - 사용자 없음")
    void createJwt_UserNotFound() {
        // given
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NoSuchElementException.class,
            () -> userAuthStrategy.createJwt(authDto));
    }

    @Test
    @DisplayName("JWT 생성 실패 - 비밀번호 불일치")
    void createJwt_InvalidPassword() {
        // given
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // when
        AuthResult<Map<String, Object>> result = userAuthStrategy.createJwt(authDto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Invalid password");
    }

    @Test
    @DisplayName("JWT 토큰 갱신 성공")
    void updateJwt_Success() {
        // given
        String refreshToken = "test.refresh.token";
        TokenUserInfo userInfo = new TokenUserInfo(TEST_EMAIL, Role.USER);
        
        when(jwtTokenProvider.validateAndGetTokenUserInfo(refreshToken)).thenReturn(userInfo);
        when(userTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(TEST_EMAIL)).thenReturn("test.refresh.token");
        when(jwtTokenProvider.createToken(TEST_EMAIL, Role.USER.toString())).thenReturn("new.access.token");

        // when
        AuthResult<Map<String, Object>> result = userAuthStrategy.updateJwt(refreshToken);

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.data())
            .containsEntry("accessToken", "new.access.token")
            .containsEntry("email", TEST_EMAIL)
            .containsEntry("role", Role.USER);
        
        verify(jwtTokenProvider).validateAndGetTokenUserInfo(refreshToken);
        verify(userTemplate).opsForValue();
        verify(valueOperations).get(TEST_EMAIL);
        verify(jwtTokenProvider).createToken(TEST_EMAIL, Role.USER.toString());
    }

    @Test
    @DisplayName("JWT 토큰 갱신 실패 - 저장된 토큰 없음")
    void updateJwt_RefreshTokenNotFound() {
        // given
        String refreshToken = "test.refresh.token";
        TokenUserInfo userInfo = new TokenUserInfo(TEST_EMAIL, Role.USER);
        
        when(jwtTokenProvider.validateAndGetTokenUserInfo(refreshToken)).thenReturn(userInfo);
        when(userTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(TEST_EMAIL)).thenReturn(null);

        // when
        AuthResult<Map<String, Object>> result = userAuthStrategy.updateJwt(refreshToken);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Refresh token not found");
    }

    @Test
    @DisplayName("Role 지원 여부 확인")
    void supports() {
        assertThat(userAuthStrategy.supports(Role.USER)).isTrue();
        assertThat(userAuthStrategy.supports(Role.SELLER)).isFalse();
        assertThat(userAuthStrategy.supports(Role.ADMIN)).isFalse();
    }

    @Test
    @DisplayName("토큰 삭제 성공")
    void deleteToken_Success() {
        // given
        String accessToken = "test.access.token";
        TokenUserInfo userInfo = new TokenUserInfo(TEST_EMAIL, Role.USER);
        
        when(jwtTokenProvider.validateAndGetTokenUserInfo(accessToken)).thenReturn(userInfo);
        when(userTemplate.delete(TEST_EMAIL)).thenReturn(true);

        // when
        AuthResult<Map<String, Object>> result = userAuthStrategy.deleteToken(accessToken);

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.data())
            .containsEntry("message", "Refresh token deleted");
        
        verify(jwtTokenProvider).validateAndGetTokenUserInfo(accessToken);
        verify(userTemplate).delete(TEST_EMAIL);
    }

    @Test
    @DisplayName("토큰 삭제 실패 - 유효하지 않은 토큰")
    void deleteToken_InvalidToken() {
        // given
        String invalidToken = "invalid.access.token";
        when(jwtTokenProvider.validateAndGetTokenUserInfo(invalidToken))
            .thenThrow(new IllegalArgumentException("Invalid token"));

        // when & then
        assertThrows(IllegalArgumentException.class,
            () -> userAuthStrategy.deleteToken(invalidToken));
        
        verify(jwtTokenProvider).validateAndGetTokenUserInfo(invalidToken);
        verify(userTemplate, never()).delete(anyString());
    }
}