package com.buysellgo.userservice.strategy.info.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.buysellgo.userservice.controller.info.dto.AdminUpdateReq;
import com.buysellgo.userservice.controller.info.dto.InfoUpdateReq;
import com.buysellgo.userservice.domain.admin.Admin;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.AdminRepository;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.strategy.info.common.InfoResult;
import com.buysellgo.userservice.strategy.info.common.InfoStrategy;

import static com.buysellgo.userservice.util.CommonConstant.*;
import com.buysellgo.userservice.controller.info.dto.UpdateType;

import org.apache.commons.lang3.ObjectUtils;

@Component
@RequiredArgsConstructor
public class AdminInfoStrategy implements InfoStrategy<Map<String, Object>> {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

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
    public InfoResult<Map<String, Object>> getList(Role role) {
        Map<String, Object> data = new HashMap<>();
        switch (role) {
            case USER -> {
                List<User> users = userRepository.findAll();
                data.put(USER_VO.getValue(), users.stream().map(
                    user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put(USER_VO.getValue(), user.toVo());
                    userMap.put(PROFILE_VO.getValue(), user.getProfile().toVo());
                    return userMap; 
                }).toList());
                return InfoResult.success(SUCCESS.getValue(), data);
            }
            case SELLER -> {
                List<Seller> sellers = sellerRepository.findAll();
                data.put(SELLER_VO.getValue(), sellers.stream().map(Seller::toVo).toList());
                return InfoResult.success(SUCCESS.getValue(), data);
            }
            default -> {
                return InfoResult.fail(INVALID_ROLE.getValue(),data);
            }

        }
        
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
