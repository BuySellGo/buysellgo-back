package com.buysellgo.userservice.service;

import com.buysellgo.userservice.controller.dto.SendType;
import com.buysellgo.userservice.service.dto.ServiceRes;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import com.buysellgo.userservice.common.configs.EmailConfig;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor        
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailConfig emailConfig;
    public ServiceRes<Map<String, Object>> generateEmail(String toEmail, SendType type, String valueByCode) {
        String subject = "";
        String text = "";

        switch (type) {
            case VERIFY:
                subject = "Email Verification";
                text = "Your verification code is: " + valueByCode;
                break;
            case PASSWORD:
                subject = "Password Reset";
                text = "Your temporary password is: " + valueByCode;
                break;
            default:
                return ServiceRes.fail("잘못된 요청입니다.", null);
        }

        return sendEmail(toEmail, subject, text);
    }

    private ServiceRes<Map<String, Object>> sendEmail(String toEmail, String subject, String text) {
        Map<String, Object> data = new HashMap<>();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(emailConfig.getUsername());
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(text, true);

            mailSender.send(message);

            data.put("message", "Email sent successfully");
            return ServiceRes.success("이메일 발송 성공", data);
        } catch (Exception e) {
            data.put("exception", e.getMessage());
            return ServiceRes.fail("이메일 발송 중 예외 발생: " + e.getMessage(), data);
        }
    }
}
