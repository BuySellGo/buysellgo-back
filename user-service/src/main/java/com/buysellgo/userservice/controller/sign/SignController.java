package com.buysellgo.userservice.controller.sign;

import com.buysellgo.userservice.common.configs.SocialLoginProperties;
import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.common.exception.CustomException;
import com.buysellgo.userservice.controller.sign.dto.*;
import com.buysellgo.userservice.domain.user.LoginType;
import com.buysellgo.userservice.strategy.forget.common.ForgetStrategy;
import com.buysellgo.userservice.strategy.sign.common.SignContext;
import com.buysellgo.userservice.strategy.sign.dto.*;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.common.SignStrategy;

import com.buysellgo.userservice.strategy.social.common.SocialLoginContext;
import com.buysellgo.userservice.strategy.social.common.SocialLoginResult;
import com.buysellgo.userservice.strategy.social.common.SocialLoginStrategy;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import static com.buysellgo.userservice.util.CommonConstant.*;
import com.buysellgo.userservice.strategy.forget.common.ForgetResult;   
import com.buysellgo.userservice.strategy.forget.common.ForgetContext;
import com.buysellgo.userservice.strategy.auth.common.AuthContext;
import com.buysellgo.userservice.strategy.auth.common.AuthResult;
import com.buysellgo.userservice.strategy.auth.common.AuthStrategy;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
public class SignController {
    private final SignContext signContext;
    private final SocialLoginContext socialLoginContext;
    private final SocialLoginProperties socialLoginProperties;
    private final ForgetContext forgetContext;
    private final AuthContext authContext;
    private String socialLoginUrl;

    @Operation(summary = "회원가입 요청(회원)")
    @PostMapping("/user")
    public ResponseEntity<CommonResDto<Map<String,Object>>> userSign(@Valid @RequestBody UserCreateReq req) {
        // 회원가입 전략을 가져와서 처리
        UserSignUpDto signUpDto = UserSignUpDto.from(req);
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(Role.USER);
        SignResult<Map<String, Object>> result = strategy.signUp(signUpDto);

        if(!result.success()) {
            throw new CustomException(result.message());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new CommonResDto<>(HttpStatus.CREATED, "회원가입 성공(회원)", result.data()));
    }

    @Operation(summary = "회원가입 요청(판매자)")
    @PostMapping("/seller")
    public ResponseEntity<CommonResDto<Map<String,Object>>> sellerSign(@Valid @RequestBody SellerCreateReq req) {
        // 판매자 회원가입 전략을 가져와서 처리
        SellerSignUpDto signUpDto = SellerSignUpDto.from(req);
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(Role.SELLER);
        SignResult<Map<String, Object>> result = strategy.signUp(signUpDto);

        if(!result.success()) {
           throw new CustomException(result.message());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new CommonResDto<>(HttpStatus.CREATED, "회원가입 성공(판매자)", result.data()));
    }

    @Operation(summary = "회원가입 요청(관리자)")
    @PostMapping("/admin")
    public ResponseEntity<CommonResDto<Map<String, Object>>> adminSign(@Valid @RequestBody AdminCreateReq req) {
        // 관리자 회원가입 전략을 가져와서 처리
        AdminSignUpDto signUpDto = AdminSignUpDto.from(req);
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(Role.ADMIN);
        SignResult<Map<String, Object>> result = strategy.signUp(signUpDto);

        if(!result.success()) {
            throw new CustomException(result.message());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new CommonResDto<>(HttpStatus.CREATED, "회원가입 성공(관리자)", result.data()));
    }

    @Operation(summary = "회원탈퇴 요청(회원)")
    @DeleteMapping("/user")
    public ResponseEntity<CommonResDto<Map<String, Object>>> userDelete(
            @RequestHeader(value = "Authorization", required = true) String accessToken) {
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new IllegalArgumentException("잘못된 Authorization 헤더 형식입니다.");
        }

        // 회원탈퇴 전략을 가져와서 처리
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(Role.USER);
        SignResult<Map<String, Object>> result = strategy.withdraw(token);

        if (!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResDto<>(HttpStatus.OK, "회원 탈퇴 완료(회원)", result.data()));
    }

    @Operation(summary = "회원탈퇴 요청(판매자)")
    @DeleteMapping("/seller")
    public ResponseEntity<CommonResDto<Map<String, Object>>> sellerDelete(
            @RequestHeader(value = "Authorization", required = true) String accessToken){
        if(!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            throw new IllegalArgumentException("잘못된 Authorization 헤더 형식입니다.");
        }

        // 판매자 회원탈퇴 전략을 가져와서 처리
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(Role.SELLER);
        SignResult<Map<String, Object>> result = strategy.withdraw(token);

        if(!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResDto<>(HttpStatus.OK,"회원 탈퇴 완료(판매자)", result.data()));
    }

    @Operation(summary = "중복 검사")
    @PostMapping("/duplicate")
    public ResponseEntity<CommonResDto<Map<String, Object>>> checkDuplicate(@Valid @RequestBody CheckDuplicateReq req) {
        // 중복 검사 전략을 가져와서 처리
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(req.role());
        SignResult<Map<String, Object>> result = strategy.duplicate(DuplicateDto.from(req));

        if(!result.success()) {
            throw new CustomException(result.message());
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResDto<>(HttpStatus.OK, "중복 검사 완료", result.data()));
    }

    @Operation(summary = "회원 활성화 및 판매자 승인(관리자)")
    @PutMapping("/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResDto<Map<String, Object>>> activate(@RequestBody ActivateReq req) {
        // 활성화 전략을 가져와서 처리
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(req.role());
        SignResult<Map<String, Object>> result = strategy.activate(ActivateDto.from(req));

        if(!result.success()) {
            throw new CustomException(result.message());
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResDto<>(HttpStatus.OK, "회원 활성화 및 판매자 승인 완료", result.data()));
    }

    @Operation(summary = "회원 비활성화 및 판매자 비활성화(관리자)")
    @PutMapping("/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResDto<Map<String, Object>>> deactivate(@RequestBody ActivateReq req) {
        // 비활성화 전략을 가져와서 처리
        SignStrategy<Map<String, Object>> strategy = signContext.getStrategy(req.role());
        SignResult<Map<String, Object>> result = strategy.deactivate(ActivateDto.from(req));

        if(!result.success()) {
            throw new CustomException(result.message());
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResDto<>(HttpStatus.OK, "회원 비활성화 및 판매자 비활성화 완료", result.data()));
    }

    @Operation(summary = "소셜 로그인(회원)")
    @GetMapping("/social")
    public ResponseEntity<CommonResDto<Map<String, Object>>> socialLogin(@RequestParam LoginType provider, @RequestParam String url) {
        socialLoginUrl = url;
        // 소셜 로그인 URL을 생성하여 반환
        String redirectUrl = socialLoginProperties.getRedirectUrl(provider);
        Map<String, Object> data = new HashMap<>();
        data.put("redirectUrl", redirectUrl);

        if(redirectUrl.equals(TYPE_NOT_SUPPORTED.getValue())) {
            throw new CustomException(TYPE_NOT_SUPPORTED.getValue());
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResDto<>(HttpStatus.OK, "소셜 로그인 링크 전달", data));
    }

    @Operation(summary = "소셜 로그인 콜백(회원)")
    @GetMapping("/{provider}")
    public void socialCallback(@PathVariable String provider, @RequestParam String code, HttpServletResponse response) throws IOException {
        // 소셜 로그인 전략을 가져와서 처리
        SocialLoginStrategy<Map<String,Object>> socialStrategy = socialLoginContext.getStrategy(provider);
        SocialLoginResult<Map<String, Object>> socialResult = socialStrategy.execute(code);

        if(!socialResult.success()) {
            throw new CustomException(socialResult.message());
        }
        // 전달받은 회원정보를 토대로 회원존재여부 확인
        ForgetStrategy<Map<String, Object>> verifyStrategy = forgetContext.getStrategy(Role.USER);
        ForgetResult<Map<String, Object>> verifyResult = verifyStrategy.forgetEmail((String) socialResult.data().get(EMAIL.getValue()));
        
        if(!verifyResult.success()) {
            // 회원 존재하지 않을 시 회원가입 후 로그인 처리    
            SignStrategy<Map<String, Object>> signStrategy = signContext.getStrategy(Role.USER);
            SignResult<Map<String, Object>> signResult = signStrategy.socialSignUp( socialResult.data().get(EMAIL.getValue()).toString(), provider);
            if(!signResult.success()) {
                throw new CustomException(signResult.message());
            }                   
        }   
        // 회원존재시 로그인 처리
        AuthStrategy<Map<String, Object>> authStrategy = authContext.getStrategy(Role.USER);
        AuthResult<Map<String, Object>> authResult = authStrategy.socialSignIn(socialResult.data().get(EMAIL.getValue()).toString());
        if(!authResult.success()) {
            throw new CustomException(authResult.message());
        }

        // 리다이렉트 URL 설정
        String token = URLEncoder.encode(authResult.message(), "UTF-8");
        String data = URLEncoder.encode(authResult.data().toString(), "UTF-8");
        String redirectUrl = socialLoginUrl + "?token=" + token + "&data=" + data;

        response.sendRedirect(redirectUrl);
    }
}
