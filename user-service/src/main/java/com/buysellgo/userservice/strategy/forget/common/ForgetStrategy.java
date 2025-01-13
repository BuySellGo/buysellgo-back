package com.buysellgo.userservice.strategy.forget.common;

import java.util.Map;

import com.buysellgo.userservice.common.entity.Role;

public interface ForgetStrategy<T extends Map<String, Object>> {

    ForgetResult<T> forgetEmail(String email);

    ForgetResult<T> forgetPassword(String email);

    boolean supports(Role role);
}
