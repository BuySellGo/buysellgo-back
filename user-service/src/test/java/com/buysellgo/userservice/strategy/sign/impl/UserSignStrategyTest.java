package com.buysellgo.userservice.strategy.sign.impl;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.user.LoginType;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.UserRepository;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.dto.SellerSignUpDto;
import com.buysellgo.userservice.strategy.sign.dto.SignUpDto;
import com.buysellgo.userservice.strategy.sign.dto.UserSignUpDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
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
class UserSignStrategyTest {

    @InjectMocks
    private UserSignStrategy userSignStrategy;

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RedisTemplate<String, Object> userTemplate;

    private UserSignUpDto signUpDto;
    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        signUpDto = new UserSignUpDto(
                "user@test.com",
                "password123!",
                "홍길동",
                "010-1234-5678",
                true,
                true,
                true,
                true
        );

        user = User.of(
                signUpDto.email(),
                "encoded_password",
                signUpDto.username(),
                signUpDto.phone(),
                LoginType.COMMON,
                Role.USER,
                signUpDto.emailCertified(),
                signUpDto.agreePICU(),
                signUpDto.agreeEmail(),
                signUpDto.agreeTOS()
        );
    }

    @Test
    @DisplayName("일반 사용자 회원가입 성공")
    void signUp_Success() {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        SignResult<Map<String, Object>> result = userSignStrategy.signUp(signUpDto);

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo(USER_CREATED.getValue());
        assertThat(result.data()).containsKey(USER_VO.getValue());
        
        User.Vo userVo = (User.Vo) result.data().get(USER_VO.getValue());
        assertThat(userVo.email()).isEqualTo(signUpDto.email());
        assertThat(userVo.username()).isEqualTo(signUpDto.username());
        assertThat(userVo.phone()).isEqualTo(signUpDto.phone());
    }

    @Test
    @DisplayName("회원가입 실패 - 잘못된 DTO 타입")
    void signUp_Fail_WrongDtoType() {
        // given
        SignUpDto wrongDto = new SellerSignUpDto(signUpDto.email(),
                signUpDto.password(),
                signUpDto.username(),
                signUpDto.phone(),
                Address.builder().build(),
                signUpDto.email(),
                signUpDto.email());

        // when
        SignResult<Map<String, Object>> result = userSignStrategy.signUp(wrongDto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo(DTO_NOT_MATCHED.getValue());
    }

    @Test
    @DisplayName("회원가입 실패 - 저장 실패")
    void signUp_Fail_SaveError() {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("저장 실패"));

        // when
        SignResult<Map<String, Object>> result = userSignStrategy.signUp(signUpDto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo("저장 실패");
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void withdraw_Success() {
        // given
        TokenUserInfo userInfo = new TokenUserInfo("user@test.com", Role.USER,1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(User.class));

        // when
        SignResult<Map<String, Object>> result = userSignStrategy.withdraw("token");

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo(USER_DELETED.getValue());
        verify(userRepository).delete(user);
        verify(userTemplate).delete(userInfo.getEmail());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 사용자 없음")
    void withdraw_Fail_UserNotFound() {
        // given
        TokenUserInfo userInfo = new TokenUserInfo("user@test.com", Role.USER,1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when
        SignResult<Map<String, Object>> result = userSignStrategy.withdraw("token");

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo(USER_NOT_FOUND.getValue());
        verify(userRepository, never()).delete(any(User.class));
        verify(userTemplate, never()).delete(anyString());
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 삭제 실패")
    void withdraw_Fail_DeleteError() {
        // given
        TokenUserInfo userInfo = new TokenUserInfo("user@test.com", Role.USER,1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("삭제 실패")).when(userRepository).delete(any(User.class));

        // when
        SignResult<Map<String, Object>> result = userSignStrategy.withdraw("token");

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo("삭제 실패");
    }

    @Test
    @DisplayName("Role 지원 여부 확인")
    void supports() {
        // when & then
        assertThat(userSignStrategy.supports(Role.USER)).isTrue();
        assertThat(userSignStrategy.supports(Role.SELLER)).isFalse();
        assertThat(userSignStrategy.supports(Role.ADMIN)).isFalse();
    }
}