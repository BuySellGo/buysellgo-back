package com.buysellgo.userservice.service;

import java.util.Random;

import com.buysellgo.userservice.common.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.RequiredArgsConstructor;  
import com.buysellgo.userservice.service.dto.ServiceRes;
import java.util.Map;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.buysellgo.userservice.common.util.CommonConstant.SUCESS;

@Service
@RequiredArgsConstructor
public class GenerateCodeService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*";
    private static final int PASSWORD_LENGTH = 8;

    private final RedisTemplate<String, Object> verificationTemplate;
    private final Random random = new Random();

    public ServiceRes<Map<String, Object>> generateVerificationCode(String email, String type) {
        Map<String, Object> data = new HashMap<>();
        try {
            String code = String.format("%06d", random.nextInt(999999));
            
            verificationTemplate.opsForValue().set(email + ":" + type, code, 5, TimeUnit.MINUTES);
            data.put("code", code);
            return ServiceRes.success("인증코드 생성 성공", data);
        } catch (Exception e) {
            return ServiceRes.fail("인증코드 생성 실패", data);
        }
    }

    public ServiceRes<Map<String, Object>> verifyCode(String email, String type, String code) {
        Map<String, Object> data = new HashMap<>();
        try {
            String storedCode = (String) verificationTemplate.opsForValue().get(email + ":" + type);
            if (storedCode != null && storedCode.equals(code)) {
                verificationTemplate.delete(email + ":" + type);
                data.put(SUCESS.getValue(), true);
                return ServiceRes.success("인증메일 검증 성공", data);
            } else {
                data.put(SUCESS.getValue(), false);
                return ServiceRes.fail("인증 코드가 일치하지 않습니다.", data);
            }
        } catch (Exception e) {
            data.put(SUCESS.getValue(), false);
            return ServiceRes.fail("인증메일 검증 실패", data);
        }
    }

    public String generateTemporaryPassword(String email, String type, Role role) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        // Add at least one digit
        password.append(DIGITS.charAt(secureRandom.nextInt(DIGITS.length())));

        // Add at least one special character
        password.append(SPECIAL_CHARACTERS.charAt(secureRandom.nextInt(SPECIAL_CHARACTERS.length())));

        // Fill the rest with random characters
        for (int i = 2; i < PASSWORD_LENGTH; i++) {
            String allCharacters = CHARACTERS + DIGITS + SPECIAL_CHARACTERS;
            password.append(allCharacters.charAt(secureRandom.nextInt(allCharacters.length())));
        }

        // Shuffle the characters to ensure randomness
        String tempPassword = shuffleString(password.toString(), secureRandom);
        verificationTemplate.opsForValue().set(email + ":" + role + ":" + type, tempPassword);
        return tempPassword;
    }

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
