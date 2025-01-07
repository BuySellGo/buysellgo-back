package com.buysellgo.userservice.strategy.sign;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.user.LoginType;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.UserRepository;
import com.buysellgo.userservice.service.dto.SellerSignUpDto;
import com.buysellgo.userservice.service.dto.UserSignUpDto;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSignStrategyTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    
    @InjectMocks
    private UserSignStrategy userSignStrategy;

    @Test
    @DisplayName("일반 회원가입 성공 케이스")
    void signUp_ValidUserDto_Success() {
        // given
        UserSignUpDto dto = new UserSignUpDto(
            "test@test.com",
            "password",
            "username",
            "01012345678",
            true,
            true,
            true,
            true
        );
        
        System.out.println("테스트 시작: 일반 회원가입");
        System.out.println("SignUp DTO: " + dto);

        User user = User.of(
            dto.email(),
            "encodedPassword",
            dto.username(),
            dto.phone(),
            LoginType.COMMON,
            Role.USER,
            dto.emailCertified(),
            dto.agreePICU(),
            dto.agreeEmail(),
            dto.agreeTOS()
        );

        System.out.println("Created User: " + user);

        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        System.out.println("회원가입 시도");
        SignResult<User.Vo> result = userSignStrategy.signUp(dto);

        // then
        System.out.println("회원가입 결과: " + result);
        assertThat(result.success()).isTrue();
        assertThat(result.data()).isNotNull();
        assertThat(result.data().email()).isEqualTo(dto.email());
        assertThat(result.data().username()).isEqualTo(dto.username());
        
        verify(passwordEncoder).encode(dto.password());
        verify(userRepository).save(any(User.class));
        System.out.println("테스트 종료: 일반 회원가입 성공");
    }

    @Test
    @DisplayName("잘못된 DTO 타입으로 회원가입 시도")
    void signUp_InvalidDtoType_Failure() {
        // given
        SellerSignUpDto dto = mock(SellerSignUpDto.class);

        // when
        SignResult<User.Vo> result = userSignStrategy.signUp(dto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Invalid DTO type for user sign up");
        
        verifyNoInteractions(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 중 예외 발생")
    void signUp_ThrowsException_Failure() {
        // given
        UserSignUpDto dto = new UserSignUpDto(
            "test@test.com",
            "password",
            "username",
            "01012345678",
            true,
            true,
            true,
            true
        );

        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("DB error"));

        // when
        SignResult<User.Vo> result = userSignStrategy.signUp(dto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("DB error");
    }

    @Test
    @DisplayName("USER 역할 지원 확인")
    void supports_UserRole_True() {
        assertThat(userSignStrategy.supports(Role.USER)).isTrue();
    }

    @Test
    @DisplayName("SELLER 역할 미지원 확인")
    void supports_SellerRole_False() {
        assertThat(userSignStrategy.supports(Role.SELLER)).isFalse();
    }

    @Test
    @DisplayName("회원 탈퇴 - 미구현 상태 확인")
    void withdraw_NotImplemented() {
        // when
        SignResult<Void> result = userSignStrategy.withdraw("token");

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Not implemented");
    }
}