package com.buysellgo.userservice.strategy.sign.common;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.strategy.sign.dto.ActivateDto;
import com.buysellgo.userservice.strategy.sign.dto.DuplicateDto;
import com.buysellgo.userservice.strategy.sign.dto.SignUpDto;

import java.util.Map;


public interface SignStrategy<T extends Map<String, Object>> {
    SignResult<T> signUp(SignUpDto dto);
    SignResult<T> withdraw(String token);
    SignResult<T> activate(ActivateDto dto);
    SignResult<T> duplicate(DuplicateDto dto);
    SignResult<T> socialSignUp();
    boolean supports(Role role);
}

