package com.buysellgo.userservice.service;

import com.buysellgo.userservice.controller.mail.dto.SendType;
import com.buysellgo.userservice.service.dto.ServiceRes;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import com.buysellgo.userservice.configs.EmailConfig;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor        
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailConfig emailConfig;

    // 이메일을 생성하고 전송하는 메서드
    public ServiceRes<Map<String, Object>> generateEmail(String toEmail, SendType type, String valueByCode) {
        String subject = ""; // 이메일 제목
        String text = ""; // 이메일 본문

        // 전송 유형에 따라 이메일 제목과 본문 설정
        switch (type) {
            case VERIFY:
                subject = "Email Verification"; // 이메일 인증
                text = "Your verification code is: " + valueByCode; // 인증 코드 포함
                break;
            case PASSWORD:
                subject = "Password Reset"; // 비밀번호 재설정
                text = "Your temporary password is: " + valueByCode; // 임시 비밀번호 포함
                break;
            default:
                return ServiceRes.fail("잘못된 요청입니다.", null);
        }

        // 이메일 전송
        return sendEmail(toEmail, subject, text);
    }

    // 실제 이메일을 전송하는 메서드
    private ServiceRes<Map<String, Object>> sendEmail(String toEmail, String subject, String text) {
        Map<String, Object> data = new HashMap<>();
        try {
            MimeMessage message = mailSender.createMimeMessage(); // MimeMessage 생성
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(emailConfig.getUsername()); // 발신자 이메일 설정
            helper.setTo(toEmail); // 수신자 이메일 설정
            helper.setSubject(subject); // 이메일 제목 설정
            helper.setText(text, true); // 이메일 본문 설정

            mailSender.send(message); // 이메일 전송

            data.put("message", "Email sent successfully");
            return ServiceRes.success("이메일 발송 성공", data);
        } catch (Exception e) {
            data.put("exception", e.getMessage());
            return ServiceRes.fail("이메일 발송 중 예외 발생: " + e.getMessage(), data);
        }
    }
}
