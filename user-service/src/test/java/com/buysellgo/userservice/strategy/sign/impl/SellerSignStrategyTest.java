package com.buysellgo.userservice.strategy.sign.impl;

import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.repository.SellerRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static com.buysellgo.userservice.common.util.CommonConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
class SellerSignStrategyTest {

    @InjectMocks
    private SellerSignStrategy sellerSignStrategy;

    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

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

}