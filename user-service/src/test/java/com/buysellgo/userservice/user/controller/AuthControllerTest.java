package com.buysellgo.userservice.user.controller;

import com.buysellgo.userservice.common.dto.CommonExceptionHandler;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.user.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.user.controller.dto.KeepLogin;
import com.buysellgo.userservice.user.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
            .standaloneSetup(authController)
            .setControllerAdvice(new CommonExceptionHandler())
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .build();
    }

    @Test
    @DisplayName("일반 사용자 JWT 토큰 생성 성공")
    void createJwt_UserSuccess() throws Exception {
        // given
        JwtCreateReq req = new JwtCreateReq(
                "test@test.com",
                "test1234!",
                KeepLogin.ACTIVE,
                Role.USER
        );

        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("accessToken", "test.access.token");
        serviceResponse.put("username", "홍길동");

        given(authService.createJwt(any(JwtCreateReq.class))).willReturn(serviceResponse);

        // when & then
        mockMvc.perform(post("/auth/jwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.statusMessage").value("로그인 성공"))
                .andExpect(jsonPath("$.result.accessToken").value("test.access.token"))
                .andExpect(jsonPath("$.result.username").value("홍길동"))
                .andExpect(jsonPath("$.result.email").value("test@test.com"))
                .andExpect(jsonPath("$.result.role").value("USER"));
    }

    @Test
    @DisplayName("이메일 누락으로 JWT 토큰 생성 실패")
    void createJwt_EmptyEmail() throws Exception {
        // given
        JwtCreateReq req = new JwtCreateReq(
                "",  // 빈 이메일
                "test1234!",
                KeepLogin.ACTIVE,
                Role.USER
        );

        // when & then
        mockMvc.perform(post("/auth/jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.email").value("이메일은 필수 입니다."));
    }

    @Test
    @DisplayName("잘못된 이메일 형식으로 JWT 토큰 생성 실패")
    void createJwt_InvalidEmail() throws Exception {
        // given
        JwtCreateReq req = new JwtCreateReq(
                "invalid-email",  // 잘못된 이메일 형식
                "test1234!",
                KeepLogin.ACTIVE,
                Role.USER
        );

        // when & then
        mockMvc.perform(post("/auth/jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.email").value("올바른 이메일 형식이 아닙니다."));
    }

    @Test
    @DisplayName("잘못된 비밀번호 형식으로 JWT 토큰 생성 실패")
    void createJwt_InvalidPassword() throws Exception {
        // given
        JwtCreateReq req = new JwtCreateReq(
                "test@test.com",
                "short",  // 잘못된 비밀번호 형식
                KeepLogin.ACTIVE,
                Role.USER
        );

        // when & then
        mockMvc.perform(post("/auth/jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.password")
                    .value("비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."));
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 정보 불일치")
    void createJwt_LoginFailed() throws Exception {
        // given
        JwtCreateReq req = new JwtCreateReq(
                "test@test.com",
                "test1234!",
                KeepLogin.ACTIVE,
                Role.USER
        );

        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("failure", true);

        given(authService.createJwt(any(JwtCreateReq.class))).willReturn(serviceResponse);

        // when & then
        mockMvc.perform(post("/auth/jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 JWT 토큰 생성 실패")
    void createJwt_UserNotFound() throws Exception {
        // given
        JwtCreateReq req = new JwtCreateReq(
                "notfound@test.com",
                "test1234!",
                KeepLogin.ACTIVE,
                Role.USER
        );

        given(authService.createJwt(any(JwtCreateReq.class)))
                .willThrow(new NoSuchElementException("사용자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/auth/jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
    }
}