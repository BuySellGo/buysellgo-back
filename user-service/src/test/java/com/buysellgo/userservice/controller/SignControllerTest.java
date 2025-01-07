package com.buysellgo.userservice.controller;

import com.buysellgo.userservice.common.dto.CommonExceptionHandler;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.dto.UserCreateReq;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.service.SignService;
import com.buysellgo.userservice.service.dto.UserSignUpDto;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class SignControllerTest {
    @Mock
    private SignService signService;
    
    @InjectMocks
    private SignController signController;
    
    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            
        mockMvc = MockMvcBuilders.standaloneSetup(signController)
            .setControllerAdvice(new CommonExceptionHandler())
            .build();
    }

    private UserCreateReq createValidRequest() {
        return new UserCreateReq(
            "test@test.com",          
            "Test1234!",              
            "username",               
            "010-1234-5678",         
            true,                     
            true,                     
            true,                     
            true                      
        );
    }

    private UserCreateReq createInvalidRequest() {
        return new UserCreateReq(
            "invalid-email",          
            "short",                  
            "",                       
            "01012345678",           
            false,                    
            false,                    
            false,                    
            false                     
        );
    }

    private User.Vo createUserVo(UserCreateReq req) {
        return new User.Vo(
            1L,
            req.email(),
            req.username(),
            req.phone(),
            "COMMON",
            "AUTHORIZED",
            "USER",
            req.emailCertified(),
            req.agreePICU(),
            req.agreeEmail(),
            req.agreeTOS(),
            System.currentTimeMillis()
        );
    }

    @Test
    @DisplayName("일반 회원가입 성공")
    void userSign_Success() throws Exception {
        // given
        UserCreateReq req = createValidRequest();
        User.Vo userVo = createUserVo(req);

        doReturn(SignResult.success(userVo))
            .when(signService)
            .signUp(any(UserSignUpDto.class), any(Role.class));

        // when & then
        mockMvc.perform(post("/sign/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.statusCode").value(201))
            .andExpect(jsonPath("$.statusMessage").value("회원가입 성공(회원)"))
            .andExpect(jsonPath("$.result.email").value(req.email()))
            .andExpect(jsonPath("$.result.username").value(req.username()));
    }

    @Test
    @DisplayName("일반 회원가입 실패 - 유효성 검사 실패")
    void userSign_ValidationFailure() throws Exception {
        // given
        UserCreateReq req = createInvalidRequest();

        // when & then
        mockMvc.perform(post("/sign/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.email").value("올바른 이메일 형식이 아닙니다."))
            .andExpect(jsonPath("$.errors.password").value("비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."))
            .andExpect(jsonPath("$.errors.username").value("닉네임은 필수 입니다."))
            .andExpect(jsonPath("$.errors.phone").value("올바른 전화번호 형식이 아닙니다."))
            .andExpect(jsonPath("$.errors.emailCertified").value("이메일 인증이 필요합니다."))
            .andExpect(jsonPath("$.errors.agreePICU").value("개인정보 수집 및 이용에 동의해야 합니다."))
            .andExpect(jsonPath("$.errors.agreeTOS").value("서비스 이용약관에 동의해야 합니다."));
    }

    @Test
    @DisplayName("일반 회원가입 실패 - 필수 필드 누락")
    void userSign_MissingRequiredFields() throws Exception {
        // given
        String invalidJson = "{}";

        // when & then
        mockMvc.perform(post("/sign/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(result -> {
                String content = result.getResponse().getContentAsString();
                assertThat(content).contains("이메일은 필수 입니다.")
                    .contains("비밀번호는 필수 입니다.")
                    .contains("닉네임은 필수 입니다.")
                    .contains("휴대폰 번호는 필수 입니다.")
                    .contains("이메일 인증 여부는 필수 입니다.")
                    .contains("개인정보 수집 및 이용 동의는 필수 입니다.")
                    .contains("이메일 수신 동의 여부는 필수 입니다.")
                    .contains("서비스 이용약관 동의는 필수 입니다.");
            });
    }

    @Test
    @DisplayName("일반 회원가입 실패 - 길이 제한 초과")
    void userSign_SizeLimitExceeded() throws Exception {
        // given
        String overSizedString = "a".repeat(51);  // 50자 제한 초과
        UserCreateReq req = new UserCreateReq(
            overSizedString + "@test.com",  // 이메일 50자 초과
            "Test1234!",
            overSizedString,                // 닉네임 50자 초과
            "010-1234-5678",
            true,
            true,
            true,
            true
        );

        // when & then
        mockMvc.perform(post("/sign/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.email").value("이메일은 50자를 초과할 수 없습니다."))
            .andExpect(jsonPath("$.errors.username").value("닉네임은 50자를 초과할 수 없습니다."));
    }
}