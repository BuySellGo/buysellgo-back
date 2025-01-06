package com.buysellgo.userservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.dto.SellerCreateReq;
import com.buysellgo.userservice.controller.dto.UserCreateReq;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.domain.user.LoginType;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SignServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignService signService;

    private UserCreateReq userCreateReq;
    private User user;
    private SellerCreateReq sellerCreateReq;
    private Seller seller;

    @BeforeEach
    void setUp() {
        // 일반 사용자 설정
        userCreateReq = new UserCreateReq(
            "test@example.com",
            "password123",
            "testuser",
            "01012345678",
            true,
            true,
            true,
            true
        );

        user = User.of(
            userCreateReq.email(),
            "encodedPassword",
            userCreateReq.username(),
            userCreateReq.phone(),
            LoginType.COMMON,
            Role.USER,
            userCreateReq.emailCertified(),
            userCreateReq.agreePICU(),
            userCreateReq.agreeEmail(),
            userCreateReq.agreeTOS()
        );

        // 판매자 설정
        Address address = Address.builder()
            .city("서울시 강남구")
            .street("테헤란로 123")
            .zipCode("06234")
            .build();

        sellerCreateReq = new SellerCreateReq(
            "부매고 주식회사",
            "홍길동",
            address,
            "seller@buysellgo.com",
            "password123",
            "123-45-67890",
            "business_registration.jpg"
        );

        seller = Seller.of(
            sellerCreateReq.companyName(),
            sellerCreateReq.presidentName(),
            sellerCreateReq.address(),
            sellerCreateReq.email(),
            "encodedPassword",
            sellerCreateReq.businessRegistrationNumber(),
            sellerCreateReq.businessRegistrationNumberImg()
        );
    }

    @Test
    @DisplayName("일반 사용자 회원가입 성공")
    void userSign_Success() {
        // given
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // when
        User.Vo result = signService.userSign(userCreateReq);

        // then
        assertNotNull(result);
        assertEquals(userCreateReq.email(), result.email());
        assertEquals(userCreateReq.username(), result.username());
        assertEquals(userCreateReq.phone(), result.phone());
        assertEquals(LoginType.COMMON.toString(), result.loginType());
        assertEquals(Role.USER.toString(), result.role());

        verify(passwordEncoder).encode(userCreateReq.password());
        verify(userRepository).save(any(User.class));
        verify(userRepository).findByUsername(userCreateReq.username());
    }

    @Test
    @DisplayName("판매자 회원가입 성공")
    void sellerSign_Success() {
        // given
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);
        when(sellerRepository.findByCompanyName(any())).thenReturn(Optional.of(seller));

        // when
        Seller.Vo result = signService.sellerSign(sellerCreateReq);

        // then
        assertNotNull(result);
        assertEquals(sellerCreateReq.companyName(), result.companyName());
        assertEquals(sellerCreateReq.presidentName(), result.presidentName());
        assertEquals(sellerCreateReq.email(), result.email());
        assertEquals(sellerCreateReq.businessRegistrationNumber(), result.businessRegistrationNumber());
        assertEquals(sellerCreateReq.businessRegistrationNumberImg(), result.businessRegistrationNumberImg());
        assertEquals(sellerCreateReq.address().toString(), result.address());

        verify(passwordEncoder).encode(sellerCreateReq.password());
        verify(sellerRepository).save(any(Seller.class));
        verify(sellerRepository).findByCompanyName(sellerCreateReq.companyName());
    }
}