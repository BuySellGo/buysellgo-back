package com.buysellgo.userservice.strategy.auth.common;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.strategy.auth.dto.AuthDto;
import java.util.Map;

public interface AuthStrategy<T extends Map<String, Object>> {
    AuthResult<T> createJwt(AuthDto dto);
    AuthResult<T> updateJwt(String token);
    AuthResult<T> deleteToken(String token);
    boolean supports(Role role);
}
