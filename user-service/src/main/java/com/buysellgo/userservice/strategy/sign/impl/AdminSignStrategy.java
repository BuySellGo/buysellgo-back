package com.buysellgo.userservice.strategy.sign.impl;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.repository.AdminRepository;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.common.SignStrategy;
import com.buysellgo.userservice.strategy.sign.dto.ActivateDto;
import com.buysellgo.userservice.strategy.sign.dto.AdminSignUpDto;
import com.buysellgo.userservice.strategy.sign.dto.DuplicateDto;
import com.buysellgo.userservice.strategy.sign.dto.SignUpDto;

import java.util.Map;
import com.buysellgo.userservice.domain.admin.Admin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import static com.buysellgo.userservice.util.CommonConstant.*;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminSignStrategy implements SignStrategy<Map<String, Object>> {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public SignResult<Map<String, Object>> signUp(SignUpDto dto) {
        Map<String, Object> data = new HashMap<>();
        try {
            if(!(dto instanceof AdminSignUpDto adminSignUpDto)){
                return SignResult.fail(DTO_NOT_MATCHED.getValue(), data);
            }

            Admin admin = Admin.of(adminSignUpDto.email(), passwordEncoder.encode(adminSignUpDto.password()), Role.ADMIN);
            data.put(ADMIN_VO.getValue(), admin.toVo());
            if(adminRepository.existsByEmail(admin.getEmail())){
                return SignResult.fail(EMAIL_DUPLICATED.getValue(), data);
            }
            adminRepository.save(admin);
            return SignResult.success(ADMIN_SIGN_UP_SUCCESS.getValue(), data);
        } catch (RuntimeException e) {
            e.setStackTrace(e.getStackTrace());
            return SignResult.fail(SAVE_FAILURE.getValue(), data);
        }
    }

    @Override
    public SignResult<Map<String, Object>> withdraw(String token) {
        return null;
    }

    @Override
    public SignResult<Map<String, Object>> activate(ActivateDto dto) {
        return null;
    }

    @Override
    public SignResult<Map<String, Object>> deactivate(ActivateDto dto) {
        return null;
    }

    @Override
    public SignResult<Map<String, Object>> duplicate(DuplicateDto dto) {
        return null;
    }

    @Override
    public SignResult<Map<String, Object>> socialSignUp(String email, String provider) {
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return Role.ADMIN.equals(role);
    }
}
