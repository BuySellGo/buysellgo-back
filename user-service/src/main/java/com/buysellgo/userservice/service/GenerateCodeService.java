package com.buysellgo.userservice.service;

import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.security.SecureRandom;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.RequiredArgsConstructor;
import com.buysellgo.userservice.service.dto.ServiceRes;    
import com.buysellgo.userservice.common.entity.Role;

import static com.buysellgo.userservice.util.CommonConstant.SUCCESS;

@Service
@RequiredArgsConstructor
public class GenerateCodeService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*";
    private static final int PASSWORD_LENGTH = 8;

    private final RedisTemplate<String, Object> verificationTemplate;
    private final Random random = new Random(); // 랜덤 객체 생성

    // 인증 코드를 생성하는 메서드
    public ServiceRes<Map<String, Object>> generateVerificationCode(String email, String type) {
        Map<String, Object> data = new HashMap<>();
        try {
            // 6자리 인증 코드 생성
            String code = String.format("%06d", random.nextInt(999999));
            
            // Redis에 인증 코드 저장 (5분 동안 유효)
            verificationTemplate.opsForValue().set(email + ":" + type, code, 5, TimeUnit.MINUTES);
            data.put("code", code);
            return ServiceRes.success("인증코드 생성 성공", data);
        } catch (Exception e) {
            return ServiceRes.fail("인증코드 생성 실패", data);
        }
    }

    // 인증 코드를 검증하는 메서드
    public ServiceRes<Map<String, Object>> verifyCode(String email, String type, String code) {
        Map<String, Object> data = new HashMap<>();
        try {
            // Redis에서 저장된 인증 코드 가져오기
            String storedCode = (String) verificationTemplate.opsForValue().get(email + ":" + type);
            if (storedCode != null && storedCode.equals(code)) {
                verificationTemplate.delete(email + ":" + type); // 인증 성공 시 코드 삭제
                data.put(SUCCESS.getValue(), true);
                return ServiceRes.success("인증메일 검증 성공", data);
            } else {
                data.put(SUCCESS.getValue(), false);
                return ServiceRes.fail("인증 코드가 일치하지 않습니다.", data);
            }
        } catch (Exception e) {
            data.put(SUCCESS.getValue(), false);
            return ServiceRes.fail("인증메일 검증 실패", data);
        }
    }

    // 임시 비밀번호를 생성하는 메서드
    public String generateTemporaryPassword(String email, String type, Role role) {
        SecureRandom secureRandom = new SecureRandom(); // 보안 랜덤 객체 생성
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        // 최소 한 개의 숫자 추가
        password.append(DIGITS.charAt(secureRandom.nextInt(DIGITS.length())));

        // 최소 한 개의 특수 문자 추가
        password.append(SPECIAL_CHARACTERS.charAt(secureRandom.nextInt(SPECIAL_CHARACTERS.length())));

        // 나머지 부분을 랜덤 문자로 채우기
        for (int i = 2; i < PASSWORD_LENGTH; i++) {
            String allCharacters = CHARACTERS + DIGITS + SPECIAL_CHARACTERS;
            password.append(allCharacters.charAt(secureRandom.nextInt(allCharacters.length())));
        }

        // 문자열을 섞어 랜덤성 보장
        String tempPassword = shuffleString(password.toString(), secureRandom);
        verificationTemplate.opsForValue().set(email + ":" + role + ":" + type, tempPassword);
        return tempPassword;
    }

    // 문자열을 섞는 메서드
    private String shuffleString(String input, SecureRandom random) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = characters[index];
            characters[index] = characters[i];
            characters[i] = temp;
        }
        return new String(characters);
    }
}