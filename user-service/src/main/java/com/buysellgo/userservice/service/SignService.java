package com.buysellgo.userservice.service;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.common.exception.WithdrawFailedException;
import com.buysellgo.userservice.service.dto.SignUpDto;
import com.buysellgo.userservice.strategy.sign.common.SignContext;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.common.SignStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SignService {
    private final SignContext signContext;

    public <T> SignResult<T> signUp(SignUpDto signUpDto, Role role) {
        SignStrategy<T> strategy = signContext.getStrategy(role);
        return strategy.signUp(signUpDto);
    }

    public void withdraw(String token, Role role) {
        SignStrategy<?> strategy = signContext.getStrategy(role);
        SignResult<Void> result = strategy.withdraw(token);
        if (!result.success()) {
            throw new WithdrawFailedException(result.errorMessage());
        }
    }
}
