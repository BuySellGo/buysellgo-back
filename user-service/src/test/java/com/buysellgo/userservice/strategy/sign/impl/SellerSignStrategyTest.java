package com.buysellgo.userservice.strategy.sign.impl;

import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.dto.SellerSignUpDto;
import com.buysellgo.userservice.strategy.sign.dto.SignUpDto;
import com.buysellgo.userservice.strategy.sign.dto.UserSignUpDto;
import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import org.springframework.data.redis.core.RedisTemplate;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
class SellerSignStrategyTest {

    @InjectMocks
    private SellerSignStrategy sellerSignStrategy;

    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RedisTemplate<String, Object> sellerTemplate;

    private SellerSignUpDto signUpDto;
    private Seller seller;

    @BeforeEach
    void setUp() {
        sellerRepository.deleteAll();
        signUpDto = new SellerSignUpDto(
                "seller@test.com",
                "password123!",
                "홍길동",
                "테스트 회사",
                new Address("서울시", "강남구", "12345"),
                "123-45-67890",
                "business_registration.jpg"
        );

        seller = Seller.of(
                signUpDto.companyName(),
                signUpDto.presidentName(),
                signUpDto.address(),
                signUpDto.email(),
                Role.SELLER,
                "encoded_password",
                signUpDto.businessRegistrationNumber(),
                signUpDto.businessRegistrationNumberImg()
        );
    }

    @Test
    @DisplayName("판매자 회원가입 성공")
    void signUp_Success() {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        // when
        SignResult<Map<String, Object>> result = sellerSignStrategy.signUp(signUpDto);

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo(SELLER_CREATED.getValue());
        assertThat(result.data()).containsKey(SELLER_VO.getValue());
        
        Seller.Vo sellerVo = (Seller.Vo) result.data().get(SELLER_VO.getValue());
        assertThat(sellerVo.email()).isEqualTo(signUpDto.email());
        assertThat(sellerVo.companyName()).isEqualTo(signUpDto.companyName());
        assertThat(sellerVo.presidentName()).isEqualTo(signUpDto.presidentName());
        assertThat(sellerVo.businessRegistrationNumber()).isEqualTo(signUpDto.businessRegistrationNumber());
    }

    @Test
    @DisplayName("판매자 회원가입 실패 - 잘못된 DTO 타입")
    void signUp_Fail_WrongDtoType() {
        // given
        SignUpDto wrongDto = new UserSignUpDto(signUpDto.email(),signUpDto.password()
                ,signUpDto.presidentName(),signUpDto.companyName(),
                true,true,true,true);

        // when
        SignResult<Map<String, Object>> result = sellerSignStrategy.signUp(wrongDto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo(DTO_NOT_MATCHED.getValue());
    }

    @Test
    @DisplayName("판매자 회원가입 실패 - 저장 실패")
    void signUp_Fail_SaveError() {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(sellerRepository.save(any(Seller.class))).thenThrow(new RuntimeException("저장 실패"));

        // when
        SignResult<Map<String, Object>> result = sellerSignStrategy.signUp(signUpDto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo("저장 실패");
    }

    @Test
    @DisplayName("Role 지원 여부 확인")
    void supports() {
        // when & then
        assertThat(sellerSignStrategy.supports(Role.SELLER)).isTrue();
        assertThat(sellerSignStrategy.supports(Role.USER)).isFalse();
        assertThat(sellerSignStrategy.supports(Role.ADMIN)).isFalse();
    }

    @Test
    @DisplayName("판매자 회원 탈퇴 �공")
    void withdraw_Success() {
        // given
        TokenUserInfo userInfo = new TokenUserInfo("seller@test.com", Role.SELLER, 1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(sellerRepository.findByEmail(anyString())).thenReturn(Optional.of(seller));
        doNothing().when(sellerRepository).delete(any(Seller.class));

        // when
        SignResult<Map<String, Object>> result = sellerSignStrategy.withdraw("token");

        // then
        assertThat(result.success()).isTrue();
        assertThat(result.message()).isEqualTo(SELLER_DELETED.getValue());
        verify(sellerRepository).delete(seller);
        verify(sellerTemplate).delete(userInfo.getEmail());
    }

    @Test
    @DisplayName("판매자 회원 탈퇴 실패 - 판매자 없음")
    void withdraw_Fail_SellerNotFound() {
        // given
        TokenUserInfo userInfo = new TokenUserInfo("seller@test.com", Role.SELLER, 1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(sellerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when
        SignResult<Map<String, Object>> result = sellerSignStrategy.withdraw("token");

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo(USER_NOT_FOUND.getValue());
        verify(sellerRepository, never()).delete(any(Seller.class));
        verify(sellerTemplate, never()).delete(anyString());
    }

    @Test
    @DisplayName("판매자 회원 탈퇴 실패 - 삭제 실패")
    void withdraw_Fail_DeleteError() {
        // given
        TokenUserInfo userInfo = new TokenUserInfo("seller@test.com", Role.SELLER, 1L);
        when(jwtTokenProvider.validateAndGetTokenUserInfo(anyString())).thenReturn(userInfo);
        when(sellerRepository.findByEmail(anyString())).thenReturn(Optional.of(seller));
        doThrow(new RuntimeException("삭제 실패")).when(sellerRepository).delete(any(Seller.class));

        // when
        SignResult<Map<String, Object>> result = sellerSignStrategy.withdraw("token");

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.message()).isEqualTo("삭제 실패");
    }
}