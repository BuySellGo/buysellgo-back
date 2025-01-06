package com.buysellgo.userservice.user.controller;

import com.buysellgo.userservice.common.dto.CommonExceptionHandler;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.user.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.user.controller.dto.KeepLogin;
import com.buysellgo.userservice.user.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
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
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

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
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer test.access.token"))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.statusMessage").value("로그인 성공"))
                .andExpect(jsonPath("$.result.username").value("홍길동"))
                .andExpect(jsonPath("$.result.email").value("test@test.com"))
                .andExpect(jsonPath("$.result.role").value("USER"));
    }

    @Test
    @DisplayName("JWT 토큰 갱신 성공")
    void refreshToken_Success() throws Exception {
        // given
        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("accessToken", "new.access.token");
        serviceResponse.put("username", "홍길동");
        serviceResponse.put("email", "test@test.com");
        serviceResponse.put("role", Role.USER);
        
        given(authService.updateJwt("test.access.token")).willReturn(serviceResponse);

        // when & then
        mockMvc.perform(put("/auth/jwt")
                .header("Authorization", "Bearer test.access.token"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer new.access.token"))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.statusMessage").value("토큰 갱신 성공"))
                .andExpect(jsonPath("$.result.username").value("홍길동"))
                .andExpect(jsonPath("$.result.email").value("test@test.com"))
                .andExpect(jsonPath("$.result.role").value("USER"));
    }

    @Test
    @DisplayName("토큰 누락으로 JWT 토큰 갱신 실패")
    void refreshToken_EmptyToken() throws Exception {
        mockMvc.perform(put("/auth/jwt"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Required request header 'Authorization' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 JWT 토큰 갱신 실패")
    void refreshToken_InvalidToken() throws Exception {
        // given
        given(authService.updateJwt("invalid.token"))
                .willThrow(new JwtException("유효하지 않은 토큰입니다."));

        // when & then
        mockMvc.perform(put("/auth/jwt")
                .header("Authorization", "Bearer invalid.token"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("유효하지 않은 토큰입니다."));
    }

    @Test
    @DisplayName("리프레시 토큰 만료 또는 사용자 없음으로 JWT 토큰 갱신 실패")
    void refreshToken_RefreshTokenExpiredOrUserNotFound() throws Exception {
        // given
        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("failure", true);
        
        given(authService.updateJwt("test.access.token")).willReturn(serviceResponse);

        // when & then
        mockMvc.perform(put("/auth/jwt")
                .header("Authorization", "Bearer test.access.token"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("리프레시 토큰이 만료되었거나, 해당 사용자가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("판매자 JWT 토큰 생성 성공")
    void createJwt_SellerSuccess() throws Exception {
        // given
        JwtCreateReq req = new JwtCreateReq(
                "seller@test.com",
                "test1234!",
                KeepLogin.ACTIVE,
                Role.SELLER
        );

        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("accessToken", "test.access.token");
        serviceResponse.put("username", "판매자");

        given(authService.createJwt(any(JwtCreateReq.class))).willReturn(serviceResponse);

        // when & then
        mockMvc.perform(post("/auth/jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer test.access.token"))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.statusMessage").value("로그인 성공"))
                .andExpect(jsonPath("$.result.username").value("판매자"))
                .andExpect(jsonPath("$.result.email").value("seller@test.com"))
                .andExpect(jsonPath("$.result.role").value("SELLER"));
    }

    @Test
    @DisplayName("관리자 JWT 토큰 생성 성공")
    void createJwt_AdminSuccess() throws Exception {
        // given
        JwtCreateReq req = new JwtCreateReq(
                "admin@test.com",
                "admin1234!",
                KeepLogin.ACTIVE,
                Role.ADMIN
        );

        Map<String, Object> serviceResponse = new HashMap<>();
        serviceResponse.put("accessToken", "test.access.token");
        serviceResponse.put("username", "관리자");

        given(authService.createJwt(any(JwtCreateReq.class))).willReturn(serviceResponse);

        // when & then
        mockMvc.perform(post("/auth/jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer test.access.token"))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.statusMessage").value("로그인 성공"))
                .andExpect(jsonPath("$.result.username").value("관리자"))
                .andExpect(jsonPath("$.result.email").value("admin@test.com"))
                .andExpect(jsonPath("$.result.role").value("ADMIN"));
    }
}