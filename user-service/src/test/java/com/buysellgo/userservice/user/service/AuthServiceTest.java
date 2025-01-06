package com.buysellgo.userservice.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.user.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.user.controller.dto.KeepLogin;
import com.buysellgo.userservice.user.domain.seller.Seller;
import com.buysellgo.userservice.user.domain.user.User;
import com.buysellgo.userservice.user.repository.SellerRepository;
import com.buysellgo.userservice.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.NoSuchElementException;
import org.springframework.data.redis.RedisConnectionFailureException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private SellerRepository sellerRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    
    @Mock
    @Qualifier("userTemplate")
    private RedisTemplate<String, Object> userTemplate;
    
    @Mock
    @Qualifier("sellerTemplate")
    private RedisTemplate<String, Object> sellerTemplate;
    
    @Mock
    @Qualifier("adminTemplate")
    private RedisTemplate<String, Object> adminTemplate;
    
    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private AuthService authService;

    private static final long KEEP_LOGIN_HOURS = 168L;  // 로그인 유지 7일

    @BeforeEach
    void setUp() {
        lenient().when(userTemplate.opsForValue()).thenReturn(valueOperations);
        lenient().when(sellerTemplate.opsForValue()).thenReturn(valueOperations);
        lenient().when(adminTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("일반 사용자 JWT 토큰 생성 성공")
    void createJwt_UserSuccess() {
        // given
        String email = "test@test.com";
        String password = "test1234!";
        String username = "홍길동";
        String encodedPassword = "encodedPassword";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        User user = User.builder()
            .email(email)
            .password(encodedPassword)
            .username(username)
            .build();

        JwtCreateReq req = new JwtCreateReq(email, password, KeepLogin.ACTIVE, Role.USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.createToken(email, Role.USER.toString())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(email, Role.USER.toString())).thenReturn(refreshToken);

        // when
        Map<String, Object> result = authService.createJwt(req);

        // then
        assertThat(result)
            .containsEntry("accessToken", accessToken)
            .containsEntry("username", username);
        
        verify(valueOperations).set(email, refreshToken, KEEP_LOGIN_HOURS, TimeUnit.HOURS);
    }

    @Test
    @DisplayName("판매자 JWT 토큰 생성 성공 (로그인 유지 포함)")
    void createJwt_SellerSuccess() {
        // given
        String email = "seller@test.com";
        String password = "test1234!";
        String companyName = "테스트회사";
        String encodedPassword = "encodedPassword";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        Seller seller = Seller.builder()
            .email(email)
            .password(encodedPassword)
            .companyName(companyName)
            .build();

        JwtCreateReq req = new JwtCreateReq(email, password, KeepLogin.ACTIVE, Role.SELLER);

        when(sellerRepository.findByEmail(email)).thenReturn(Optional.of(seller));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.createToken(email, Role.SELLER.toString())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(email, Role.SELLER.toString())).thenReturn(refreshToken);

        // when
        Map<String, Object> result = authService.createJwt(req);

        // then
        assertThat(result)
            .containsEntry("accessToken", accessToken)
            .containsEntry("companyName", companyName);
        
        verify(valueOperations).set(email, refreshToken, KEEP_LOGIN_HOURS, TimeUnit.HOURS);
    }

    @Test
    @DisplayName("관리자 JWT 토큰 생성 성공")
    void createJwt_AdminSuccess() {
        // given
        String email = "admin@test.com";
        String password = "test1234!";
        String username = "관리자";
        String encodedPassword = "encodedPassword";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        User admin = User.builder()
            .email(email)
            .password(encodedPassword)
            .username(username)
            .build();

        JwtCreateReq req = new JwtCreateReq(email, password, KeepLogin.ACTIVE, Role.ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.createToken(email, Role.ADMIN.toString())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(email, Role.ADMIN.toString())).thenReturn(refreshToken);

        // when
        Map<String, Object> result = authService.createJwt(req);

        // then
        assertThat(result)
            .containsEntry("accessToken", accessToken)
            .containsEntry("username", username);
        
        verify(valueOperations).set(email, refreshToken, KEEP_LOGIN_HOURS, TimeUnit.HOURS);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 JWT 토큰 생성 실패")
    void createJwt_WrongPassword() {
        // given
        String email = "test@test.com";
        String wrongPassword = "wrongPassword";
        String encodedPassword = "encodedPassword";

        User user = User.builder()
            .email(email)
            .password(encodedPassword)
            .username("홍길동")
            .build();

        JwtCreateReq req = new JwtCreateReq(email, wrongPassword, KeepLogin.ACTIVE, Role.USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);

        // when
        Map<String, Object> result = authService.createJwt(req);

        // then
        assertThat(result)
            .containsEntry("failure", "login fail");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 JWT 토큰 생성 실패")
    void createJwt_EmailNotFound() {
        // given
        String nonExistentEmail = "nonexistent@test.com";
        String password = "test1234!";

        JwtCreateReq req = new JwtCreateReq(nonExistentEmail, password, KeepLogin.ACTIVE, Role.USER);

        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.createJwt(req))
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("Redis 저장 실패시 JWT 토큰 생성 실패")
    void createJwt_RedisStorageFailed() {
        // given
        String email = "test@test.com";
        String password = "test1234!";
        String username = "홍길동";
        String encodedPassword = "encodedPassword";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        User user = User.builder()
            .email(email)
            .password(encodedPassword)
            .username(username)
            .build();

        JwtCreateReq req = new JwtCreateReq(email, password, KeepLogin.ACTIVE, Role.USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.createToken(email, Role.USER.toString())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(email, Role.USER.toString())).thenReturn(refreshToken);
        doThrow(new RedisConnectionFailureException("Redis connection failed"))
            .when(valueOperations)
            .set(email, refreshToken, KEEP_LOGIN_HOURS, TimeUnit.HOURS);

        // when & then
        assertThatThrownBy(() -> authService.createJwt(req))
            .isInstanceOf(RedisConnectionFailureException.class)
            .hasMessage("Redis connection failed");
    }

    @Test
    @DisplayName("일반 사용자 JWT 토큰 생성 성공 - 기본 로그인")
    void createJwt_UserSuccess_WithoutKeepLogin() {
        // given
        String email = "test@test.com";
        String password = "test1234!";
        String username = "홍길동";
        String encodedPassword = "encodedPassword";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        User user = User.builder()
            .email(email)
            .password(encodedPassword)
            .username(username)
            .build();

        JwtCreateReq req = new JwtCreateReq(email, password, KeepLogin.INACTIVE, Role.USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.createToken(email, Role.USER.toString())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(email, Role.USER.toString())).thenReturn(refreshToken);

        // when
        Map<String, Object> result = authService.createJwt(req);

        // then
        assertThat(result)
            .containsEntry("accessToken", accessToken)
            .containsEntry("username", username);
        
        verify(valueOperations).set(email, refreshToken, 10L, TimeUnit.HOURS); // 10시간
    }

    @Test
    @DisplayName("일반 사용자 JWT 토큰 업데이트 성공")
    void updateJwt_UserSuccess() {
        // given
        String email = "test@test.com";
        String username = "홍길동";
        String accessToken = "test.access.token";
        String newAccessToken = "newAccessToken";
        String refreshToken = "refreshToken";

        User user = User.builder()
                .email(email)
                .username(username)
                .build();

        TokenUserInfo userInfo = TokenUserInfo.builder()
                .email(email)
                .role(Role.USER)
                .build();

        when(jwtTokenProvider.validateAndGetTokenUserInfo(accessToken)).thenReturn(userInfo);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userTemplate.opsForValue().get(email)).thenReturn(refreshToken);
        when(jwtTokenProvider.createToken(email, Role.USER.toString())).thenReturn(newAccessToken);

        // when
        Map<String, Object> result = authService.updateJwt(accessToken);

        // then
        assertThat(result)
                .containsEntry("accessToken", newAccessToken)
                .containsEntry("username", username);
        
        verify(jwtTokenProvider).validateAndGetTokenUserInfo(accessToken);
        verify(userRepository).findByEmail(email);
        verify(userTemplate.opsForValue()).get(email);
        verify(jwtTokenProvider).createToken(email, Role.USER.toString());
    }

    @Test
    @DisplayName("판매자 JWT 토큰 업데이트 성공")
    void updateJwt_SellerSuccess() {
        // given
        String email = "seller@test.com";
        String companyName = "테스트회사";
        String accessToken = "test.access.token";
        String newAccessToken = "newAccessToken";
        String refreshToken = "refreshToken";

        Seller seller = Seller.builder()
                .email(email)
                .companyName(companyName)
                .build();

        TokenUserInfo userInfo = TokenUserInfo.builder()
                .email(email)
                .role(Role.SELLER)
                .build();

        when(jwtTokenProvider.validateAndGetTokenUserInfo(accessToken)).thenReturn(userInfo);
        when(sellerRepository.findByEmail(email)).thenReturn(Optional.of(seller));
        when(sellerTemplate.opsForValue().get(email)).thenReturn(refreshToken);
        when(jwtTokenProvider.createToken(email, Role.SELLER.toString())).thenReturn(newAccessToken);

        // when
        Map<String, Object> result = authService.updateJwt(accessToken);

        // then
        assertThat(result)
                .containsEntry("accessToken", newAccessToken)
                .containsEntry("companyName", companyName);
        
        verify(jwtTokenProvider).validateAndGetTokenUserInfo(accessToken);
        verify(sellerRepository).findByEmail(email);
        verify(sellerTemplate.opsForValue()).get(email);
        verify(jwtTokenProvider).createToken(email, Role.SELLER.toString());
    }

    @Test
    @DisplayName("관리자 JWT 토큰 업데이트 성공")
    void updateJwt_AdminSuccess() {
        // given
        String email = "admin@test.com";
        String username = "관리자";
        String accessToken = "test.access.token";
        String newAccessToken = "newAccessToken";
        String refreshToken = "refreshToken";

        User admin = User.builder()
                .email(email)
                .username(username)
                .build();

        TokenUserInfo userInfo = TokenUserInfo.builder()
                .email(email)
                .role(Role.ADMIN)
                .build();

        when(jwtTokenProvider.validateAndGetTokenUserInfo(accessToken)).thenReturn(userInfo);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(admin));
        when(userTemplate.opsForValue().get(email)).thenReturn(refreshToken);
        when(jwtTokenProvider.createToken(email, Role.ADMIN.toString())).thenReturn(newAccessToken);

        // when
        Map<String, Object> result = authService.updateJwt(accessToken);

        // then
        assertThat(result)
                .containsEntry("accessToken", newAccessToken)
                .containsEntry("username", username);
        
        verify(jwtTokenProvider).validateAndGetTokenUserInfo(accessToken);
        verify(userRepository).findByEmail(email);
        verify(userTemplate.opsForValue()).get(email);
        verify(jwtTokenProvider).createToken(email, Role.ADMIN.toString());
    }

    @Test
    @DisplayName("리프레시 토큰이 없을 때 JWT 토큰 업데이트 실패")
    void updateJwt_RefreshTokenNotFound() {
        // given
        String email = "test@test.com";
        String accessToken = "test.access.token";

        TokenUserInfo userInfo = TokenUserInfo.builder()
                .email(email)
                .role(Role.USER)
                .build();

        User user = User.builder()
                .email(email)
                .username("홍길동")
                .build();

        when(jwtTokenProvider.validateAndGetTokenUserInfo(accessToken)).thenReturn(userInfo);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userTemplate.opsForValue().get(email)).thenReturn(null);

        // when
        Map<String, Object> result = authService.updateJwt(accessToken);

        // then
        assertThat(result).containsEntry("failure", "RefreshToken not found");
    }

    @Test
    @DisplayName("토큰 검증 실패시 JWT 토큰 업데이트 실패")
    void updateJwt_InvalidToken() {
        // given
        String invalidToken = "invalid.token";

        when(jwtTokenProvider.validateAndGetTokenUserInfo(invalidToken))
                .thenThrow(new JwtException("유효하지 않은 토큰입니다."));

        // when & then
        assertThatThrownBy(() -> authService.updateJwt(invalidToken))
                .isInstanceOf(JwtException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 JWT 토큰 업데이트 실패")
    void updateJwt_EmailNotFound() {
        // given
        String email = "nonexistent@test.com";
        String accessToken = "test.access.token";

        TokenUserInfo userInfo = TokenUserInfo.builder()
                .email(email)
                .role(Role.USER)
                .build();

        when(jwtTokenProvider.validateAndGetTokenUserInfo(accessToken)).thenReturn(userInfo);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.updateJwt(accessToken))
                .isInstanceOf(NoSuchElementException.class);
    }
}