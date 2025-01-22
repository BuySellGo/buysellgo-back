package com.buysellgo.userservice.strategy.forget.impl;

import static com.buysellgo.userservice.util.CommonConstant.USER_NOT_FOUND;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.mail.dto.SendType;
import com.buysellgo.userservice.service.GenerateCodeService;
import com.buysellgo.userservice.service.dto.ServiceRes;
import com.buysellgo.userservice.strategy.forget.common.ForgetResult;
import com.buysellgo.userservice.strategy.forget.common.ForgetStrategy;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.service.EmailService;

@Component
@Transactional
@RequiredArgsConstructor
public class SellerForgetStrategy implements ForgetStrategy<Map<String, Object>> {
    private final SellerRepository sellerRepository;
    private final EmailService emailService;
    private final GenerateCodeService generateCodeService;
    private final RedisTemplate<String, Object> verificationTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ForgetResult<Map<String, Object>> forgetEmail(String email) {
        Map<String, Object> data = new HashMap<>();
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if (seller.isPresent()) {
            data.put("email", seller.get().getEmail());
            return ForgetResult.success("이메일 찾기 성공", data);
        }
        return ForgetResult.fail("이메일 찾기 실패",data);
    }

    @Override
    public ForgetResult<Map<String, Object>> forgetPassword(String email) {
        Map<String, Object> data = new HashMap<>();
        Optional<Seller> sellerOptional = sellerRepository.findByEmail(email);
        if(sellerOptional.isEmpty()) {
            return ForgetResult.fail(USER_NOT_FOUND.getValue(), data);
        }
        Seller seller = sellerOptional.get();
        ServiceRes<Map<String, Object>> res = emailService.generateEmail(email, SendType.PASSWORD
                , generateCodeService.generateTemporaryPassword(email, SendType.PASSWORD.toString(), Role.SELLER));
        data.put("res", res);
        if(!res.success()){
            return ForgetResult.fail("메세지 전송 실패",data);
        }
        Object tempPassword= ObjectUtils.defaultIfNull(verificationTemplate.opsForValue().get(email + ":" + Role.SELLER + ":" + SendType.PASSWORD), null);
        if(ObjectUtils.anyNull(tempPassword)){
            return ForgetResult.fail("임시 비밀번호를 찾을 수 없습니다.", data);
        }

        verificationTemplate.delete(email + ":" + Role.SELLER + ":" + SendType.PASSWORD);

        data.put("password(개발중이라 넣었습니다.)", tempPassword);
        seller.setPassword(passwordEncoder.encode(tempPassword.toString()));

        return ForgetResult.success("메세지 전송 성공",data);
    }

    @Override
    public boolean supports(Role role) {
        return role.equals(Role.SELLER);
    }
}
