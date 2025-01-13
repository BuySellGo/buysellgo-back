package com.buysellgo.userservice.strategy.forget.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.strategy.forget.common.ForgetResult;
import com.buysellgo.userservice.strategy.forget.common.ForgetStrategy;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.domain.seller.Seller;


@Component
@Transactional
@RequiredArgsConstructor
public class SellerForgetStrategy implements ForgetStrategy<Map<String, Object>> {
    private final SellerRepository sellerRepository;

    @Override
    public ForgetResult<Map<String, Object>> forgetEmail(String email) {
        Map<String, Object> data = new HashMap<>();
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if (seller.isPresent()) {
            data.put("email", seller.get().getEmail());
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
        return role.equals(Role.SELLER);
    }
}
