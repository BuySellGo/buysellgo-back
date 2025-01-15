package com.buysellgo.userservice.controller;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.auth.dto.JwtCreateReq;
import com.buysellgo.userservice.controller.auth.dto.KeepLogin;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효한 로그인 요청 검증")
    void validateJwtCreateReq_ValidRequest() {
        // given
        JwtCreateReq req = new JwtCreateReq(
            "test@test.com",
            "test1234!",
            KeepLogin.ACTIVE,
            Role.USER
        );

        // when
        Set<ConstraintViolation<JwtCreateReq>> violations = validator.validate(req);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이메일 누락 시 검증")
    void validateJwtCreateReq_EmptyEmail() {
        // given
        JwtCreateReq req = new JwtCreateReq(
            "",
            "test1234!",
            KeepLogin.ACTIVE,
            Role.USER
        );

        // when
        Set<ConstraintViolation<JwtCreateReq>> violations = validator.validate(req);

        // then
        assertThat(violations)
            .hasSize(1)
            .anyMatch(violation -> 
                violation.getMessage().equals("이메일은 필수 입니다."));
    }

    @Test
    @DisplayName("잘못된 이메일 형식 검증")
    void validateJwtCreateReq_InvalidEmailFormat() {
        // given
        JwtCreateReq req = new JwtCreateReq(
            "invalid-email",
            "test1234!",
            KeepLogin.ACTIVE,
            Role.USER
        );

        // when
        Set<ConstraintViolation<JwtCreateReq>> violations = validator.validate(req);

        // then
        assertThat(violations)
            .hasSize(1)
            .anyMatch(violation -> 
                violation.getMessage().equals("올바른 이메일 형식이 아닙니다."));
    }

    @Test
    @DisplayName("비밀번호 누락 시 검증")
    void validateJwtCreateReq_EmptyPassword() {
        // given
        JwtCreateReq req = new JwtCreateReq(
            "test@test.com",
            "",
            KeepLogin.ACTIVE,
            Role.USER
        );

        // when
        Set<ConstraintViolation<JwtCreateReq>> violations = validator.validate(req);

        // then
        assertThat(violations)
            .hasSize(1)
            .anyMatch(violation -> 
                violation.getMessage().equals("비밀번호는 필수 입니다."));
    }

    @Test
    @DisplayName("비밀번호 형식 검증 - 특수문자 누락")
    void validateJwtCreateReq_PasswordWithoutSpecialChar() {
        // given
        JwtCreateReq req = new JwtCreateReq(
            "test@test.com",
            "test1234",
            KeepLogin.ACTIVE,
            Role.USER
        );

        // when
        Set<ConstraintViolation<JwtCreateReq>> violations = validator.validate(req);

        // then
        assertThat(violations)
            .hasSize(1)
            .anyMatch(violation -> 
                violation.getMessage().equals("비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."));
    }

    @Test
    @DisplayName("비밀번호 형식 검증 - 숫자 누락")
    void validateJwtCreateReq_PasswordWithoutNumber() {
        // given
        JwtCreateReq req = new JwtCreateReq(
            "test@test.com",
            "test!!!@",
            KeepLogin.ACTIVE,
            Role.USER
        );

        // when
        Set<ConstraintViolation<JwtCreateReq>> violations = validator.validate(req);

        // then
        assertThat(violations)
            .hasSize(1)
            .anyMatch(violation -> 
                violation.getMessage().equals("비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."));
    }

    @Test
    @DisplayName("비밀번호 형식 검증 - 8자 미만")
    void validateJwtCreateReq_PasswordTooShort() {
        // given
        JwtCreateReq req = new JwtCreateReq(
            "test@test.com",
            "test1!",
            KeepLogin.ACTIVE,
            Role.USER
        );

        // when
        Set<ConstraintViolation<JwtCreateReq>> violations = validator.validate(req);

        // then
        assertThat(violations)
            .hasSize(1)
            .anyMatch(violation -> 
                violation.getMessage().equals("비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."));
    }

    @Test
    @DisplayName("자동 로그인 설정 누락 검증")
    void validateJwtCreateReq_NullKeepLogin() {
        // given
        JwtCreateReq req = new JwtCreateReq(
            "test@test.com",
            "test1234!",
            null,
            Role.USER
        );

        // when
        Set<ConstraintViolation<JwtCreateReq>> violations = validator.validate(req);

        // then
        assertThat(violations)
            .hasSize(1)
            .anyMatch(violation -> 
                violation.getMessage().equals("자동 로그인 사용 여부는 필수 입니다."));
    }

    @Test
    @DisplayName("사용자 역할 누락 검증")
    void validateJwtCreateReq_NullRole() {
        // given
        JwtCreateReq req = new JwtCreateReq(
            "test@test.com",
            "test1234!",
            KeepLogin.ACTIVE,
            null
        );

        // when
        Set<ConstraintViolation<JwtCreateReq>> violations = validator.validate(req);

        // then
        assertThat(violations)
            .hasSize(1)
            .anyMatch(violation -> 
                violation.getMessage().equals("사용자 역할은 필수 입니다."));
    }
}