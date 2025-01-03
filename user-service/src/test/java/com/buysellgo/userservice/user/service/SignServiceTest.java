package com.buysellgo.userservice.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.user.controller.dto.UserCreateReq;
import com.buysellgo.userservice.user.domain.user.LoginType;
import com.buysellgo.userservice.user.domain.user.User;
import com.buysellgo.userservice.user.repository.UserRepository;
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
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignService signService;

    private UserCreateReq userCreateReq;
    private User user;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    @DisplayName("회원가입 성공")
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
}