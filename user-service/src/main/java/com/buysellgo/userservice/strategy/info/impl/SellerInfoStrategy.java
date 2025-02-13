package com.buysellgo.userservice.strategy.info.impl;

import java.util.Map;

import com.buysellgo.userservice.controller.info.dto.InfoUpdateReq;
import com.buysellgo.userservice.controller.info.dto.SellerUpdateReq;
import com.buysellgo.userservice.domain.seller.Seller;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.strategy.info.common.InfoResult;
import com.buysellgo.userservice.strategy.info.common.InfoStrategy;

import static com.buysellgo.userservice.util.CommonConstant.*;

import java.util.HashMap;
import java.util.Optional;

import com.buysellgo.userservice.repository.SellerRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SellerInfoStrategy implements InfoStrategy<Map<String, Object>> {
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public InfoResult<Map<String, Object>> getOne(String email) {
        Map<String, Object> data = new HashMap<>();
        Optional<Seller> sellerOptional = sellerRepository.findByEmail(email);
        if(sellerOptional.isEmpty()) {
            return InfoResult.fail(USER_NOT_FOUND.getValue(),data);
        }
        Seller seller = sellerOptional.get();
        data.put(SELLER_VO.getValue(),seller.toVo());
        return InfoResult.success(SUCCESS.getValue(),data);
    }

    @Override
    public InfoResult<Map<String, Object>> getList(Role role) {
        return null;
    }

    @Override
    public InfoResult<Map<String, Object>> edit(InfoUpdateReq req, String email) {
        Map<String, Object> data = new HashMap<>();
        SellerUpdateReq sellerReq = (SellerUpdateReq) req;
        Optional<Seller> sellerOptional = sellerRepository.findByEmail(email);
        if(sellerOptional.isEmpty()) {
            return InfoResult.fail(USER_NOT_FOUND.getValue(),data);
        }
        Seller seller = sellerOptional.get();
        switch (sellerReq.getType()) {
            case INFO -> {
                handleInfoUpdate(sellerReq, seller, data);
                if(data.isEmpty()) {
                    return InfoResult.fail(NO_CHANGE.getValue(),data);
                }

            }
            case PASSWORD -> {
                handlePasswordUpdate(sellerReq, seller, data);
                if(data.isEmpty()) {
                    return InfoResult.fail(NO_CHANGE.getValue(),data);
                }
            }
            default -> {
                return InfoResult.fail("잘못된 요청입니다.",data);
            }
        }
        return InfoResult.success(UPDATE_SUCCESS.getValue(),data);
    }

    private void handleInfoUpdate(SellerUpdateReq sellerReq, Seller seller, Map<String, Object> data) {
        if(ObjectUtils.isNotEmpty(sellerReq.getCompanyName())) {
            seller.setCompanyName(sellerReq.getCompanyName());
            data.put("companyName",seller.getCompanyName());
        }
        if(ObjectUtils.isNotEmpty(sellerReq.getPresidentName())) {
            seller.setPresidentName(sellerReq.getPresidentName());
            data.put("presidentName",seller.getPresidentName());
        }
        if(ObjectUtils.isNotEmpty(sellerReq.getAddress())) {
            seller.setAddress(sellerReq.getAddress());
            data.put("address",seller.getAddress());
        }
    }

    private void handlePasswordUpdate(SellerUpdateReq sellerReq, Seller seller, Map<String, Object> data) {
        if(ObjectUtils.isNotEmpty(sellerReq.getPassword())) {
            seller.setPassword(passwordEncoder.encode(sellerReq.getPassword()));
            data.put(SELLER_VO.getValue(),seller.toVo());
        }
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.SELLER;
    }
}