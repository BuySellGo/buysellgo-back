package com.buysellgo.userservice.controller.mail;

import com.buysellgo.userservice.common.exception.CustomException;
import com.buysellgo.userservice.controller.mail.dto.MailSendReq;
import com.buysellgo.userservice.controller.mail.dto.SendType;
import com.buysellgo.userservice.controller.mail.dto.VerifyCodeReq;
import com.buysellgo.userservice.service.dto.ServiceRes;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.service.EmailService;
import com.buysellgo.userservice.service.GenerateCodeService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;
    private final GenerateCodeService verificationService;

    @Operation(summary = "회원가입 시 이메일 인증")
    @PostMapping("/send")
    public ResponseEntity<CommonResDto<Object>> sendEmail(@Valid @RequestBody MailSendReq mailSendReq) {
        if(!mailSendReq.type().equals(SendType.VERIFY)){
            throw new CustomException("요청의 형식이 올바르지 않습니다.");
        }
        // 인증 코드를 생성하고 이메일로 전송
        ServiceRes<Map<String, Object>> verificationCode = verificationService.generateVerificationCode(mailSendReq.email(), mailSendReq.type().toString());
        emailService.generateEmail(mailSendReq.email(), mailSendReq.type(), verificationCode.data().get("code").toString());
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "인증메일 발송 성공", verificationCode));
    }

    @Operation(summary = "이메일 인증 코드 검증")
    @DeleteMapping("/verify")
    public ResponseEntity<CommonResDto<Object>> verifyMail(@Valid @RequestBody VerifyCodeReq verifyCodeReq) {
        // 이메일 인증 코드를 검증
        ServiceRes<Map<String, Object>> verificationCode = verificationService.verifyCode(verifyCodeReq.email(),verifyCodeReq.type().toString(), verifyCodeReq.code());
        if (verificationCode.success()) {
            return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "인증메일 검증 성공", verificationCode));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CommonResDto<>(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다.", verificationCode));
        }
    }
}