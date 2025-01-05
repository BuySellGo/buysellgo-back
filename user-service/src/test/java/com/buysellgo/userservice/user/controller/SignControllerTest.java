package com.buysellgo.userservice.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.buysellgo.userservice.common.dto.CommonExceptionHandler;
import com.buysellgo.userservice.common.entity.Authorization;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.user.controller.dto.UserCreateReq;
import com.buysellgo.userservice.user.domain.user.LoginType;
import com.buysellgo.userservice.user.domain.user.User;
import com.buysellgo.userservice.user.service.SignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.http.HttpStatus;
import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.user.controller.dto.SellerCreateReq;
import com.buysellgo.userservice.user.domain.seller.Seller;

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
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
            .standaloneSetup(signController)
            .setControllerAdvice(new CommonExceptionHandler())
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void userSign_Success() throws Exception {
        // given
        String requestBody = """
            {
                "email": "test@test.com",
                "password": "test1234!",
                "username": "홍길동",
                "phone": "010-1234-5678",
                "emailCertified": true,
                "agreePICU": true,
                "agreeEmail": true,
                "agreeTOS": true
            }
            """;

        User.Vo userVo = new User.Vo(
            1L,
            "test@test.com",
            "홍길동",
            "010-1234-5678",
            LoginType.COMMON.toString(),
            Authorization.AUTHORIZED.toString(),
            Role.USER.toString(),
            true,
            true,
            true,
            true,
            1L
        );

        given(signService.userSign(any(UserCreateReq.class))).willReturn(userVo);

        // when & then
        mockMvc.perform(post("/sign/user")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.statusCode").value(201))
            .andExpect(jsonPath("$.statusMessage").value("회원가입 성공(회원)"))
            .andExpect(jsonPath("$.result.email").value("test@test.com"))
            .andExpect(jsonPath("$.result.username").value("홍길동"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "test@", "@example.com"})
    @DisplayName("잘못된 이메일 형식")
    void userSign_InvalidEmailFormat(String invalidEmail) throws Exception {
        // given
        UserCreateReq req = new UserCreateReq(
            invalidEmail,
            "test1234!",
            "홍길동",
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
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.email")
                    .value("올바른 이메일 형식이 아닙니다."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test123", "test!", "short", "nospecial1"})
    @DisplayName("잘못된 비밀번호 형식")
    void userSign_InvalidPasswordFormat(String invalidPassword) throws Exception {
        // given
        UserCreateReq req = new UserCreateReq(
            "test@test.com",
            invalidPassword,
            "홍길동",
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
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.password")
                .value("비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."));
    }

    @Test
    @DisplayName("필수 동의 항목 누락")
    void userSign_MissingRequiredAgreements() throws Exception {
        // given
        UserCreateReq req = new UserCreateReq(
            "test@test.com",
            "test1234!",
            "홍길동",
            "010-1234-5678",
            true,
            false,  // agreePICU false
            true,
            false   // agreeTOS false
        );

        // when & then
        mockMvc.perform(post("/sign/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.errors.agreePICU")
                .value("개인정보 수집 및 이용에 동의해야 합니다."))
            .andExpect(jsonPath("$.errors.agreeTOS")
                .value("서비스 이용약관에 동의해야 합니다."));
    }

    @Test
    @DisplayName("이메일 인증 누락")
    void userSign_MissingEmailCertification() throws Exception {
        // given
        UserCreateReq req = new UserCreateReq(
            "test@test.com",
            "test1234!",
            "홍길동",
            "010-1234-5678",
            false,  // emailCertified false
            true,
            true,
            true
        );

        // when & then
        mockMvc.perform(post("/sign/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.emailCertified")
                .value("이메일 인증이 필요합니다."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"011-1234-567", "010-12345-1234", "01012345678", "02-1234-5678"})
    @DisplayName("잘못된 전화번호 형식")
    void userSign_InvalidPhoneFormat(String invalidPhone) throws Exception {
        // given
        UserCreateReq req = new UserCreateReq(
            "test@test.com",
            "test1234!",
            "홍길동",
            invalidPhone,
            true,
            true,
            true,
            true
        );

        // when & then
        mockMvc.perform(post("/sign/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.phone")
                .value("올바른 휴대폰 번호 형식이 아닙니다."));
    }

    @Test
    @DisplayName("필수 필드 누락")
    void userSign_MissingRequiredFields() throws Exception {
        // given
        String requestBody = """
            {
                "email": "",
                "password": "",
                "username": "",
                "phone": "",
                "emailCertified": null,
                "agreePICU": null,
                "agreeEmail": true,
                "agreeTOS": null
            }
            """;

        // when & then
        mockMvc.perform(post("/sign/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.email").value("이메일은 필수 입니다."))
            .andExpect(jsonPath("$.errors.password").value("비밀번호는 필수 입니다."))
            .andExpect(jsonPath("$.errors.username").value("닉네임은 필수 입니다."))
            .andExpect(jsonPath("$.errors.phone").value("휴대폰 번호는 필수 입니다."))
            .andExpect(jsonPath("$.errors.emailCertified").value("이메일 인증 여부는 필수 입니다."))
            .andExpect(jsonPath("$.errors.agreePICU").value("개인정보 수집 및 이용 동의는 필수 입니다."))
            .andExpect(jsonPath("$.errors.agreeTOS").value("서비스 이용약관 동의는 필수 입니다."));
    }

    @Test
    @DisplayName("판매자 회원가입 성공")
    void sellerSign_Success() throws Exception {
        // given
        Address address = Address.builder()
            .city("서울시 강남구")
            .street("테헤란로 123")
            .zipCode("06234")
            .build();

        String requestBody = """
            {
                "companyName": "부매고 주식회사",
                "presidentName": "홍길동",
                "address": {
                    "city": "서울시 강남구",
                    "street": "테헤란로 123",
                    "zipCode": "06234"
                },
                "email": "seller@buysellgo.com",
                "password": "test1234!",
                "businessRegistrationNumber": "123-45-67890",
                "businessRegistrationNumberImg": "business_registration.jpg"
            }
            """;

        Seller.Vo sellerVo = new Seller.Vo(
            1L,
            "부매고 주식회사",
            "홍길동",
            address.toString(),
            "seller@buysellgo.com",
            "encodedPassword",
            "123-45-67890",
            "business_registration.jpg",
            Authorization.UNAUTHORIZED.toString()
        );

        given(signService.sellerSign(any(SellerCreateReq.class))).willReturn(sellerVo);

        // when & then
        mockMvc.perform(post("/sign/seller")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.statusCode").value(201))
            .andExpect(jsonPath("$.statusMessage").value("회원가입 성공(판매자)"))
            .andExpect(jsonPath("$.result.companyName").value("부매고 주식회사"))
            .andExpect(jsonPath("$.result.presidentName").value("홍길동"))
            .andExpect(jsonPath("$.result.email").value("seller@buysellgo.com"))
            .andExpect(jsonPath("$.result.businessRegistrationNumber").value("123-45-67890"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345-67890", "123-456-7890", "1234567890"})
    @DisplayName("잘못된 사업자등록번호 형식")
    void sellerSign_InvalidBusinessRegistrationNumber(String invalidBRN) throws Exception {
        // given
        Address address = Address.builder()
            .city("서울시 강남구")
            .street("테헤란로 123")
            .zipCode("06234")
            .build();

        SellerCreateReq req = new SellerCreateReq(
            "부매고 주식회사",
            "홍길동",
            address,
            "seller@buysellgo.com",
            "test1234!",
            invalidBRN,
            "business_registration.jpg"
        );

        // when & then
        mockMvc.perform(post("/sign/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.businessRegistrationNumber")
                .value("올바른 사업자등록번호 형식이 아닙니다."));
    }

    @Test
    @DisplayName("판매자 필수 필드 누락")
    void sellerSign_MissingRequiredFields() throws Exception {
        // given
        String requestBody = """
            {
                "companyName": "",
                "presidentName": "",
                "address": {
                    "city": "",
                    "street": "",
                    "zipCode": ""
                },
                "email": "",
                "password": "",
                "businessRegistrationNumber": "",
                "businessRegistrationNumberImg": ""
            }
            """;

        // when & then
        mockMvc.perform(post("/sign/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.companyName").value("회사명은 필수 입니다."))
            .andExpect(jsonPath("$.errors.presidentName").value("대표자명은 필수 입니다."))
            .andExpect(jsonPath("$.errors.email").value("이메일은 필수 입니다."))
            .andExpect(jsonPath("$.errors.password").value("비밀번호는 필수 입니다."))
            .andExpect(jsonPath("$.errors.businessRegistrationNumber").value("사업자등록번호는 필수 입니다."))
            .andExpect(jsonPath("$.errors.businessRegistrationNumberImg").value("사업자등록증 이미지는 필수 입니다."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "seller@", "@buysellgo.com"})
    @DisplayName("판매자 - 잘못된 이메일 형식")
    void sellerSign_InvalidEmailFormat(String invalidEmail) throws Exception {
        // given
        Address address = Address.builder()
            .city("서울시 강남구")
            .street("테헤란로 123")
            .zipCode("06234")
            .build();

        SellerCreateReq req = new SellerCreateReq(
            "부매고 주식회사",
            "홍길동",
            address,
            invalidEmail,
            "test1234!",
            "123-45-67890",
            "business_registration.jpg"
        );

        // when & then
        mockMvc.perform(post("/sign/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.email")
                .value("올바른 이메일 형식이 아닙니다."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test123", "test!", "short", "nospecial1"})
    @DisplayName("판매자 - 잘못된 비밀번호 형식")
    void sellerSign_InvalidPasswordFormat(String invalidPassword) throws Exception {
        // given
        Address address = Address.builder()
            .city("서울시 강남구")
            .street("테헤란로 123")
            .zipCode("06234")
            .build();

        SellerCreateReq req = new SellerCreateReq(
            "부매고 주식회사",
            "홍길동",
            address,
            "seller@buysellgo.com",
            invalidPassword,
            "123-45-67890",
            "business_registration.jpg"
        );

        // when & then
        mockMvc.perform(post("/sign/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.errors.password")
                .value("비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."));
    }
}