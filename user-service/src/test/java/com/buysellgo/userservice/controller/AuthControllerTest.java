package com.buysellgo.userservice.controller;

import com.buysellgo.userservice.common.dto.CommonExceptionHandler;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.dto.JwtCreateReq;
import com.buysellgo.userservice.controller.dto.KeepLogin;
import com.buysellgo.userservice.service.AuthService;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
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
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "password123!";

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
                TEST_EMAIL,
                TEST_PASSWORD,
                KeepLogin.ACTIVE,
                Role.USER
        );

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", "test.access.token");
        tokenInfo.put("username", "홍길동");
        tokenInfo.put("email", TEST_EMAIL);
        tokenInfo.put("role", Role.USER);

        @SuppressWarnings("rawtypes")
        AuthResult serviceResponse = AuthResult.success(tokenInfo);
        given(authService.createJwt(any())).willReturn(serviceResponse);

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
                .andExpect(jsonPath("$.result.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.result.role").value("USER"));
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

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", "test.access.token");
        tokenInfo.put("username", "판매자");
        tokenInfo.put("email", "seller@test.com");
        tokenInfo.put("role", Role.SELLER);

        @SuppressWarnings("rawtypes")
        AuthResult serviceResponse = AuthResult.success(tokenInfo);
        given(authService.createJwt(any())).willReturn(serviceResponse);

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

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("accessToken", "test.access.token");
        tokenInfo.put("username", "관리자");
        tokenInfo.put("email", "admin@test.com");
        tokenInfo.put("role", Role.ADMIN);

        @SuppressWarnings("rawtypes")
        AuthResult serviceResponse = AuthResult.success(tokenInfo);
        given(authService.createJwt(any())).willReturn(serviceResponse);

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

    @Test
    @DisplayName("JWT 토큰 생성 실패 - 잘못된 이메일 형식")
    void createJwt_InvalidEmailFormat() throws Exception {
        // given
        JwtCreateReq req = new JwtCreateReq(
            "invalid-email",  // 잘못된 이메일 형식
            TEST_PASSWORD,
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
    @DisplayName("JWT 토큰 생성 실패 - 잘못된 비밀번호 형식")
    void createJwt_InvalidPasswordFormat() throws Exception {
        // given
        JwtCreateReq req = new JwtCreateReq(
            TEST_EMAIL,
            "short",  // 최소 8자 미만, 숫자와 특수문자 없음
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
                .andExpect(jsonPath("$.errors.password").value("비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."));
    }

    @Test
    @DisplayName("JWT 토큰 생성 실패 - 필수 필드 누락")
    void createJwt_MissingRequiredFields() throws Exception {
        // given
        Map<String, Object> req = new HashMap<>();
        req.put("email", "");  // 빈 이메일
        req.put("password", TEST_PASSWORD);
        req.put("keepLogin", KeepLogin.ACTIVE);
        // role 필드 누락

        // when & then
        mockMvc.perform(post("/auth/jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.email").value("이메일은 필수 입니다."))
                .andExpect(jsonPath("$.errors.role").value("사용자 역할은 필수 입니다."));
    }

    @Test
    @DisplayName("JWT 토큰 생성 실패 - 잘못된 KeepLogin 값")
    void createJwt_InvalidKeepLoginValue() throws Exception {
        // given
        String requestBody = """
            {
                "email": "test@test.com",
                "password": "password123!",
                "keepLogin": "INVALID_VALUE",
                "role": "USER"
            }
            """;

        // when & then
        mockMvc.perform(post("/auth/jwt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.errors.keepLogin").value("허용되지 않는 값입니다. (허용값: ACTIVE, INACTIVE)"));
    }
}