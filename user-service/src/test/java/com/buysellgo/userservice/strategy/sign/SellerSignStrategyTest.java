package com.buysellgo.userservice.strategy.sign;

import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.repository.SellerRepository;
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
class SellerSignStrategyTest {
    @Mock
    private SellerRepository sellerRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private SellerSignStrategy sellerSignStrategy;

    @Test
    @DisplayName("판매자 회원가입 성공 케이스")
    void signUp_ValidSellerDto_Success() {
        // given
        Address address = Address.builder()
            .city("서울시")
            .street("강남구 테헤란로")
            .zipCode("06234")
            .build();

        SellerSignUpDto dto = new SellerSignUpDto(
            "test@company.com",      // email
            "Test1234!",            // password
            "홍길동",               // presidentName
            "테스트 회사",          // companyName
            address,                // address
            "123-45-67890",        // businessRegistrationNumber
            "business_registration.jpg"  // businessRegistrationNumberImg
        );
        
        System.out.println("테스트 시작: 판매자 회원가입");
        System.out.println("SignUp DTO: " + dto);

        Seller seller = Seller.of(
            dto.companyName(),
            dto.presidentName(),
            dto.address(),
            dto.email(),
            "encodedPassword",
            dto.businessRegistrationNumber(),
            dto.businessRegistrationNumberImg()
        );

        System.out.println("Created Seller: " + seller);

        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        // when
        System.out.println("회원가입 시도");
        SignResult<Seller.Vo> result = sellerSignStrategy.signUp(dto);

        // then
        System.out.println("회원가입 결과: " + result);
        assertThat(result.success()).isTrue();
        assertThat(result.data()).isNotNull();
        assertThat(result.data().email()).isEqualTo(dto.email());
        assertThat(result.data().companyName()).isEqualTo(dto.companyName());
        assertThat(result.data().presidentName()).isEqualTo(dto.presidentName());
        assertThat(result.data().businessRegistrationNumber()).isEqualTo(dto.businessRegistrationNumber());
        
        verify(passwordEncoder).encode(dto.password());
        verify(sellerRepository).save(any(Seller.class));
        System.out.println("테스트 종료: 판매자 회원가입 성공");
    }

    @Test
    @DisplayName("잘못된 DTO 타입으로 회원가입 시도")
    void signUp_InvalidDtoType_Failure() {
        // given
        UserSignUpDto dto = mock(UserSignUpDto.class);

        // when
        SignResult<Seller.Vo> result = sellerSignStrategy.signUp(dto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Invalid DTO type for seller sign up");
        
        verifyNoInteractions(sellerRepository, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 중 예외 발생")
    void signUp_ThrowsException_Failure() {
        // given
        Address address = Address.builder()
            .city("서울시")
            .street("강남구 테헤란로")
            .zipCode("06234")
            .build();

        SellerSignUpDto dto = new SellerSignUpDto(
            "test@company.com",      // email
            "Test1234!",            // password
            "테길동",               // presidentName
            "테스트 회사",          // companyName
            address,                // address
            "123-45-67890",        // businessRegistrationNumber
            "business_registration.jpg"  // businessRegistrationNumberImg
        );

        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPassword");
        when(sellerRepository.save(any(Seller.class))).thenThrow(new RuntimeException("DB error"));

        // when
        SignResult<Seller.Vo> result = sellerSignStrategy.signUp(dto);

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("DB error");
    }

    @Test
    @DisplayName("SELLER 역할 지원 확인")
    void supports_SellerRole_True() {
        assertThat(sellerSignStrategy.supports(Role.SELLER)).isTrue();
    }

    @Test
    @DisplayName("USER 역할 미지원 확인")
    void supports_UserRole_False() {
        assertThat(sellerSignStrategy.supports(Role.USER)).isFalse();
    }

    @Test
    @DisplayName("판매자 탈퇴 - 미구현 상태 확인")
    void withdraw_NotImplemented() {
        // when
        SignResult<Void> result = sellerSignStrategy.withdraw("token");

        // then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Not implemented");
    }
}