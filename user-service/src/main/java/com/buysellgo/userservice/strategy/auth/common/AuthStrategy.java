package com.buysellgo.userservice.strategy.auth.common;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.service.dto.AuthDto;

public interface AuthStrategy<T> {
    AuthResult<T> createJwt(AuthDto authDto);
    AuthResult<T> updateJwt(String token);
    AuthResult<T> deleteToken(String token);
    boolean supports(Role role);
}
