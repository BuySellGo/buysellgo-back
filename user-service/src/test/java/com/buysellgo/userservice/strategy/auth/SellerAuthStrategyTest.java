package com.buysellgo.userservice.strategy.auth;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.dto.KeepLogin;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.repository.SellerRepository;
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
class SellerAuthStrategyTest {
    @InjectMocks
    private SellerAuthStrategy sellerAuthStrategy;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisTemplate<String, Object> sellerTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private Seller seller;
    private AuthDto authDto;
    private static final String TEST_EMAIL = "seller@test.com";
    private static final String TEST_PASSWORD = "password123!";
    private static final String TEST_COMPANY_NAME = "testCompany";

    @BeforeEach
    void setUp() {
        seller = mock(Seller.class);
        authDto = new AuthDto(
            TEST_EMAIL,
            TEST_PASSWORD,
            Role.SELLER,
            KeepLogin.INACTIVE
        );
    }

    @Test
    @DisplayName("JWT 생성 성공")
    void createJwt_Success() {
        // given
        when(sellerTemplate.opsForValue()).thenReturn(valueOperations);
        when(seller.getEmail()).thenReturn(TEST_EMAIL);
        when(seller.getCompanyName()).thenReturn(TEST_COMPANY_NAME);
        when(sellerRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(seller));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtTokenProvider.createToken(TEST_EMAIL, Role.SELLER.toString())).thenReturn("accessToken");
        when(jwtTokenProvider.createRefreshToken(TEST_EMAIL, Role.SELLER.toString())).thenReturn("refreshToken");

        // when
        AuthResult<Map<String, Object>> result = sellerAuthStrategy.createJwt(authDto);

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.data())
            .containsEntry("accessToken", "accessToken")
            .containsEntry("email", TEST_EMAIL)
            .containsEntry("companyName", TEST_COMPANY_NAME)
            .containsEntry("role", Role.SELLER);

        verify(valueOperations).set(
            TEST_EMAIL,
            "refreshToken",
            168L,
            TimeUnit.HOURS
        );
    }

    @Test
    @DisplayName("JWT 생성 실패 - 판매자 없음")
    void createJwt_SellerNotFound() {
        // given
        when(sellerRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NoSuchElementException.class,
            () -> sellerAuthStrategy.createJwt(authDto));
    }

    @Test
    @DisplayName("JWT 생성 실패 - 비밀번호 불일치")
    void createJwt_InvalidPassword() {
        // given
        when(sellerRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(seller));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // when
        AuthResult<Map<String, Object>> result = sellerAuthStrategy.createJwt(authDto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Invalid password");
    }

    @Test
    @DisplayName("JWT 토큰 갱신 성공")
    void updateJwt_Success() {
        // given
        String refreshToken = "test.refresh.token";
        TokenUserInfo userInfo = new TokenUserInfo(TEST_EMAIL, Role.SELLER);
        
        when(jwtTokenProvider.validateAndGetTokenUserInfo(refreshToken)).thenReturn(userInfo);
        when(sellerTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(TEST_EMAIL)).thenReturn("test.refresh.token");
        when(jwtTokenProvider.createToken(TEST_EMAIL, Role.SELLER.toString())).thenReturn("new.access.token");

        // when
        AuthResult<Map<String, Object>> result = sellerAuthStrategy.updateJwt(refreshToken);

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.data())
            .containsEntry("accessToken", "new.access.token")
            .containsEntry("email", TEST_EMAIL)
            .containsEntry("role", Role.SELLER);
        
        verify(jwtTokenProvider).validateAndGetTokenUserInfo(refreshToken);
        verify(sellerTemplate).opsForValue();
        verify(valueOperations).get(TEST_EMAIL);
        verify(jwtTokenProvider).createToken(TEST_EMAIL, Role.SELLER.toString());
    }

    @Test
    @DisplayName("JWT 토큰 갱신 실패 - 저장된 토큰 없음")
    void updateJwt_RefreshTokenNotFound() {
        // given
        String refreshToken = "test.refresh.token";
        TokenUserInfo userInfo = new TokenUserInfo(TEST_EMAIL, Role.SELLER);
        
        when(jwtTokenProvider.validateAndGetTokenUserInfo(refreshToken)).thenReturn(userInfo);
        when(sellerTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(TEST_EMAIL)).thenReturn(null);

        // when
        AuthResult<Map<String, Object>> result = sellerAuthStrategy.updateJwt(refreshToken);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Refresh token not found");
    }

    @Test
    @DisplayName("Role 지원 여부 확인")
    void supports() {
        assertThat(sellerAuthStrategy.supports(Role.SELLER)).isTrue();
        assertThat(sellerAuthStrategy.supports(Role.USER)).isFalse();
        assertThat(sellerAuthStrategy.supports(Role.ADMIN)).isFalse();
    }
}