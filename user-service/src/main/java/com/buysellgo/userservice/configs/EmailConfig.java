package com.buysellgo.userservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

import lombok.Getter;

@Configuration
@Getter 
public class EmailConfig {

    // application.yml에서 SMTP 호스트 설정을 가져옴
    @Value("${spring.mail.host}")
    private String host;

    // SMTP 포트 설정을 가져옴
    @Value("${spring.mail.port}")
    private int port;

    // SMTP 사용자 이름 설정을 가져옴
    @Value("${spring.mail.username}")
    private String username;

    // SMTP 비밀번호 설정을 가져옴
    @Value("${spring.mail.password}")
    private String password;

    // SMTP 인증 사용 여부 설정을 가져옴
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    // SMTP STARTTLS 사용 여부 설정을 가져옴
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    // SMTP 타임아웃 설정을 가져옴
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    // JavaMailSender 빈을 생성하여 Spring 컨텍스트에 등록
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host); // SMTP 호스트 설정
        mailSender.setPort(port); // SMTP 포트 설정
        mailSender.setUsername(username); // SMTP 사용자 이름 설정
        mailSender.setPassword(password); // SMTP 비밀번호 설정

        // 추가적인 SMTP 속성 설정
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp"); // 전송 프로토콜 설정
        props.put("mail.smtp.auth", auth); // SMTP 인증 사용 설정
        props.put("mail.smtp.starttls.enable", starttlsEnable); // STARTTLS 사용 설정
        props.put("mail.smtp.timeout", timeout); // 타임아웃 설정
        props.put("mail.debug", "true"); // 디버깅 모드 활성화

        return mailSender; // 설정된 JavaMailSender 반환
    }
}