package com.buysellgo.userservice.controller;

import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.controller.dto.UserCreateReq;
import com.buysellgo.userservice.controller.dto.SellerCreateReq;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SignControllerTest {

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
}