package com.buysellgo.userservice.strategy.forget.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.strategy.forget.common.ForgetResult;
import com.buysellgo.userservice.strategy.forget.common.ForgetStrategy;
import com.buysellgo.userservice.repository.UserRepository;
import com.buysellgo.userservice.domain.user.User;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UserForgetStrategy implements ForgetStrategy<Map<String, Object>> {
    private final UserRepository userRepository;
    
    @Override
    public ForgetResult<Map<String, Object>> forgetEmail(String email) {
        Map<String, Object> data = new HashMap<>();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            data.put("email", user.get().getEmail());
            return ForgetResult.success("이메일 찾기 성공", data);
        }
        return ForgetResult.fail("이메일 찾기 실패");
    }

    @Override
    public ForgetResult<Map<String, Object>> forgetPassword(String email) {
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return role.equals(Role.USER);
    }
}
