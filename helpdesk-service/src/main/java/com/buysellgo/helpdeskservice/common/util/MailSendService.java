package com.buysellgo.helpdeskservice.common.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailSendService {

    // EmailConfig에 선언한 객체가 주입됨.
    private final JavaMailSender mailSender;

    // 난수 발생 메서드
    private int makeRandomNumber() {
        // 난수의 범위: 111111 ~ 999999
        int checkNum = (int) ((Math.random() * 999999) + 111111);
        log.info("인증번호: {}", checkNum);
        return checkNum;
    }

    // 가입할 회원에게 전송할 이메일 양식 준비
    // 이 메서드를 컨트롤러가 호출할 겁니다.
    public String joinMail(String email) throws MessagingException { // 컨트롤러로부터 이메일을 전달받음.
        int authNum = makeRandomNumber();
        String setFrom = "111returns1@gmail.com"; // 발신용 이메일 주소(properties랑 똑같아야 함!)
        String toMail = email; // 수신받을 이메일 (가입하고자 하는 사람의 이메일)
        String title = "Traveler 인증 이메일 입니다."; // 실제 이메일 제목
        String content =
                "인증 번호는 <strong>" + authNum + "</strong> 입니다. <br>" +
                "해당 인증 번호를 인증번호 확인란에 기입해 주세요."; // 이메일에 삽입할 내용 (더 꾸며보세요)


        mailSend(setFrom, toMail, title, content);


        // 컨트롤러에게 인증번호 리턴(문자열)
        return Integer.toString(authNum);
    }

    // 여기서 실제 이메일이 전송
    private void mailSend(String setFrom, String toMail, String title, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        /*
            기타 설정들을 담당할 MimeMessageHelper 객체를 생성
            생성자의 매개값으로 MimeMessage 객체, bool, 문자 인코딩 설정
            true 매개값을 전달하면 MultiPart 형식의 메세지 전달이 가능 (첨부 파일)
        */
        MimeMessageHelper helper
                = new MimeMessageHelper(message, false, "utf-8");
        helper.setFrom(setFrom);
        helper.setTo(toMail);
        helper.setSubject(title);

        // 내용 채우기 (true를 전송하면 html이 포함되어 있다. 값을 안주면 단순 텍스트로만 전달됨.)
        helper.setText(content, true);

        // 메일 전송
        mailSender.send(message);
    }


}


















