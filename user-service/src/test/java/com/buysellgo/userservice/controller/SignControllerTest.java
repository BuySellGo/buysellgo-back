package com.buysellgo.userservice.controller;

import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.common.exception.CustomException;
import com.buysellgo.userservice.controller.sign.dto.UserCreateReq;
import com.buysellgo.userservice.controller.sign.dto.SellerCreateReq;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.sign.SignController;
import com.buysellgo.userservice.strategy.sign.common.SignContext;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.common.SignStrategy;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class SignControllerTest {

    @InjectMocks
    private SignController signController;

    @Mock
    private SignContext signContext;

    @Mock
    private SignStrategy<Map<String, Object>> signStrategy;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("일반 회원 가입 검증 테스트")
    class UserSignUpValidation {
        @Test
        @DisplayName("유효한 회원가입 요청 검증")
        void validateUserCreateReq_ValidRequest() {
            // given
            UserCreateReq req = new UserCreateReq(
                "test@test.com",
                "test1234!",
                "홍길동",
                "010-1234-5678",
                true,
                true,
                true,
                true
            );

            // when
            Set<ConstraintViolation<UserCreateReq>> violations = validator.validate(req);

            // then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("이메일 형식 검증")
        void validateUserCreateReq_InvalidEmail() {
            // given
            UserCreateReq req = new UserCreateReq(
                "invalid-email",
                "test1234!",
                "홍길동",
                "010-1234-5678",
                true,
                true,
                true,
                true
            );

            // when
            Set<ConstraintViolation<UserCreateReq>> violations = validator.validate(req);

            // then
            assertThat(violations)
                .hasSize(1)
                .anyMatch(violation -> 
                    violation.getMessage().equals("올바른 이메일 형식이 아닙니다."));
        }

        @Test
        @DisplayName("휴대폰 번호 형식 검증")
        void validateUserCreateReq_InvalidPhone() {
            // given
            UserCreateReq req = new UserCreateReq(
                "test@test.com",
                "test1234!",
                "홍길동",
                "01012345678",
                true,
                true,
                true,
                true
            );

            // when
            Set<ConstraintViolation<UserCreateReq>> violations = validator.validate(req);

            // then
            assertThat(violations)
                .hasSize(1)
                .anyMatch(violation -> 
                    violation.getMessage().equals("올바른 전화번호 형식이 아닙니다."));
        }
    }

    @Nested
    @DisplayName("판매자 회원 가입 검증 테스트")
    class SellerSignUpValidation {
        @Test
        @DisplayName("유효한 판매자 회원가입 요청 검증")
        void validateSellerCreateReq_ValidRequest() {
            // given
            Address address = new Address("12345", "서울시 강남구", "상세주소");
            SellerCreateReq req = new SellerCreateReq(
                "부매고 주식회사",
                "홍길동",
                address,
                "seller@buysellgo.com",
                "test1234!",
                "123-45-67890",
                "business_registration.jpg"
            );

            // when
            Set<ConstraintViolation<SellerCreateReq>> violations = validator.validate(req);

            // then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("사업자등록번호 형식 검증")
        void validateSellerCreateReq_InvalidBusinessNumber() {
            // given
            Address address = new Address("12345", "서울시 강남구", "상세주소");
            SellerCreateReq req = new SellerCreateReq(
                "부매고 주식회사",
                "홍길동",
                address,
                "seller@buysellgo.com",
                "test1234!",
                "12345678901",
                "business_registration.jpg"
            );

            // when
            Set<ConstraintViolation<SellerCreateReq>> violations = validator.validate(req);

            // then
            assertThat(violations)
                .hasSize(1)
                .anyMatch(violation -> 
                    violation.getMessage().equals("올바른 사업자등록번호 형식이 아닙니다."));
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class WithdrawTest {
        @Test
        @DisplayName("일반 회원 탈퇴 성공")
        void userWithdraw_Success() {
            // given
            String token = "Bearer valid_token";
            Map<String, Object> data = new HashMap<>();
            when(signContext.getStrategy(Role.USER)).thenReturn(signStrategy);
            when(signStrategy.withdraw(anyString()))
                .thenReturn(SignResult.success("회원 탈퇴 완료", data));

            // when
            ResponseEntity<?> response = signController.userDelete(token);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(CommonResDto.class);
        }

        @Test
        @DisplayName("판매자 회원 탈퇴 성공")
        void sellerWithdraw_Success() {
            // given
            String token = "Bearer valid_token";
            Map<String, Object> data = new HashMap<>();
            when(signContext.getStrategy(Role.SELLER)).thenReturn(signStrategy);
            when(signStrategy.withdraw(anyString()))
                .thenReturn(SignResult.success("회원 탈퇴 완료", data));

            // when
            ResponseEntity<?> response = signController.sellerDelete(token);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(CommonResDto.class);
        }

        @Test
        @DisplayName("잘못된 토큰 형식으로 인한 탈퇴 실패")
        void withdraw_Fail_InvalidTokenFormat() {
            // given
            String invalidToken = "invalid_token_format";

            // when & then
            assertThatThrownBy(() -> signController.userDelete(invalidToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 Authorization 헤더 형식입니다.");
        }

        @Test
        @DisplayName("회원 탈퇴 실패 - 실패 응답")
        void withdraw_Fail_WithdrawFailed() {
            // given
            String token = "Bearer valid_token";
            when(signContext.getStrategy(Role.USER)).thenReturn(signStrategy);
            when(signStrategy.withdraw(anyString()))
                .thenReturn(SignResult.fail("탈퇴 실패", new HashMap<>()));

            // when & then
            assertThatThrownBy(() -> signController.userDelete(token))
                .isInstanceOf(CustomException.class)
                .hasMessage("탈퇴 실패");
        }
    }
}