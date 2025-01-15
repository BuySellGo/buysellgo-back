package com.buysellgo.userservice.strategy.info.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.buysellgo.userservice.controller.info.dto.AdminUpdateReq;
import com.buysellgo.userservice.controller.info.dto.InfoUpdateReq;
import com.buysellgo.userservice.domain.admin.Admin;
import com.buysellgo.userservice.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.strategy.info.common.InfoResult;
import com.buysellgo.userservice.strategy.info.common.InfoStrategy;

import static com.buysellgo.userservice.common.util.CommonConstant.*;
import com.buysellgo.userservice.controller.info.dto.UpdateType;

import org.apache.commons.lang3.ObjectUtils;

@Component
@RequiredArgsConstructor
public class AdminInfoStrategy implements InfoStrategy<Map<String, Object>> {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public InfoResult<Map<String, Object>> getOne(String email) {
        Map<String, Object> data = new HashMap<>();
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if(adminOptional.isEmpty()) {   
            return InfoResult.fail(USER_NOT_FOUND.getValue(),data);
        }
        Admin admin = adminOptional.get();
        data.put(ADMIN_VO.getValue(),admin.toVo());
        return InfoResult.success(SUCCESS.getValue(),data);
    }

    @Override
    public InfoResult<Map<String, Object>> getList() {
        return null;
    }   

    @Override
    public InfoResult<Map<String, Object>> edit(InfoUpdateReq req, String email) {
        Map<String, Object> data = new HashMap<>();
        AdminUpdateReq adminReq = (AdminUpdateReq) req;
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if(adminOptional.isEmpty()) {
            return InfoResult.fail(USER_NOT_FOUND.getValue(),data);
        }
        Admin admin = adminOptional.get();
        
        if (adminReq.getType().toString().equals(UpdateType.PASSWORD.toString())) {
            handlePasswordUpdate(adminReq, admin, data);
            if(data.isEmpty()) {
                return InfoResult.fail(NO_CHANGE.getValue(),data);
            }
            data.put(ADMIN_VO.getValue(),admin.toVo());
            return InfoResult.success(UPDATE_SUCCESS.getValue(),data);
        } else {
            return InfoResult.fail("잘못된 요청입니다.",data);
        }
    }

    private void handlePasswordUpdate(AdminUpdateReq adminReq, Admin admin, Map<String, Object> data) {
        if(ObjectUtils.isNotEmpty(adminReq.getPassword())) {
            admin.setPassword(passwordEncoder.encode(adminReq.getPassword()));
            data.put(ADMIN_VO.getValue(),admin.toVo());
        }
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.ADMIN;
    }
}
