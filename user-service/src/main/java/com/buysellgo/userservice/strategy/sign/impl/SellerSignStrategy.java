package com.buysellgo.userservice.strategy.sign.impl;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.strategy.sign.dto.ActivateDto;
import com.buysellgo.userservice.strategy.sign.dto.DuplicateDto;
import com.buysellgo.userservice.strategy.sign.dto.SignUpDto;
import com.buysellgo.userservice.strategy.sign.dto.SellerSignUpDto;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.common.SignStrategy;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static com.buysellgo.userservice.common.util.CommonConstant.*;

@Component
@Transactional
@RequiredArgsConstructor
public class SellerSignStrategy implements SignStrategy<Map<String,Object>> {
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public SignResult<Map<String,Object>> signUp(SignUpDto dto) {
        Map<String,Object> data = new HashMap<>();
        try {
            if(!(dto instanceof SellerSignUpDto sellerSignUpDto)) {
                return SignResult.fail(DTO_NOT_MATCHED.getValue(),data);
            }

            Seller seller = Seller.of(sellerSignUpDto.companyName(),
                    sellerSignUpDto.presidentName(),
                    sellerSignUpDto.address(),
                    sellerSignUpDto.email(),
                    passwordEncoder.encode(sellerSignUpDto.password()),
                    sellerSignUpDto.businessRegistrationNumber(),
                    sellerSignUpDto.businessRegistrationNumberImg());

            data.put(SELLER_VO.getValue(),seller.toVo());

            if(sellerRepository.existsByEmail(seller.getEmail()) ||
                    sellerRepository.existsByCompanyName(seller.getCompanyName()) ||
                    sellerRepository.existsByBusinessRegistrationNumber(seller.getBusinessRegistrationNumber())){
                return SignResult.fail(VALUE_DUPLICATED.getValue(),data);
            }
            sellerRepository.save(seller);
            return SignResult.success(SELLER_CREATED.getValue(),data);
        } catch (RuntimeException e) {
            e.setStackTrace(e.getStackTrace());
            return SignResult.fail(SAVE_FAILURE.getValue(), data);
        }
    }

    @Override
    public SignResult<Map<String,Object>> withdraw(String token) {
        return null;
    }

    @Override
    public SignResult<Map<String,Object>> activate(ActivateDto dto) {
        return null;
    }

    @Override
    public SignResult<Map<String,Object>> duplicate(DuplicateDto dto) {
        return null;
    }

    @Override
    public SignResult<Map<String,Object>> socialSignUp() {
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return Role.SELLER.equals(role);
    }
}
