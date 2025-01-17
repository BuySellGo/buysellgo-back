package com.buysellgo.userservice.strategy.sign.impl;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Authorization;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.buysellgo.userservice.util.CommonConstant.*;

@Component
@Transactional
@RequiredArgsConstructor
public class SellerSignStrategy implements SignStrategy<Map<String,Object>> {
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> sellerTemplate;

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
                    Role.SELLER,
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
        Map<String, Object> data = new HashMap<>();
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        Optional<Seller> sellerOptional = sellerRepository.findByEmail(userInfo.getEmail());

        if(sellerOptional.isEmpty()){
            return SignResult.fail(USER_NOT_FOUND.getValue(),data);
        }
        Seller seller = sellerOptional.get();
        data.put(SELLER_VO.getValue(),seller.toVo());

        try{
            sellerRepository.delete(seller);
            sellerTemplate.delete(userInfo.getEmail());
        } catch (Exception e) {
            return SignResult.fail(e.getMessage(),data);
        }

        return SignResult.success(SELLER_DELETED.getValue(),data);
    }

    @Override
    public SignResult<Map<String,Object>> activate(ActivateDto dto) {
        if(!dto.role().equals(Role.SELLER)){
            return SignResult.fail(ROLE_NOT_MATCHED.getValue(), new HashMap<>());
        }

        if(!sellerRepository.existsByEmail(dto.email())){
            return SignResult.fail(USER_NOT_FOUND.getValue(), new HashMap<>());
        }

        Optional<Seller> sellerOptional = sellerRepository.findByEmail(dto.email());
        if(sellerOptional.isEmpty()){
            return SignResult.fail(USER_NOT_FOUND.getValue(), new HashMap<>());
        }
        Seller seller=sellerOptional.get();
        seller.setIsApproved(Authorization.AUTHORIZED);
        sellerRepository.save(seller);

        return SignResult.success(SELLER_ACTIVATED.getValue(), new HashMap<>());
    }

    @Override
    public SignResult<Map<String,Object>> deactivate(ActivateDto dto) {
        if(!dto.role().equals(Role.SELLER)){
            return SignResult.fail(ROLE_NOT_MATCHED.getValue(), new HashMap<>());
        }

        Optional<Seller> sellerOptional = sellerRepository.findByEmail(dto.email());
        if(sellerOptional.isEmpty()){
            return SignResult.fail(USER_NOT_FOUND.getValue(), new HashMap<>());
        }
        Seller seller=sellerOptional.get();
        seller.setIsApproved(Authorization.UNAUTHORIZED);
        sellerRepository.save(seller);

        return SignResult.success(SELLER_DEACTIVATED.getValue(), new HashMap<>());
    }

    @Override
    public SignResult<Map<String,Object>> duplicate(DuplicateDto dto) {
        if(!dto.role().equals(Role.SELLER)){
            return SignResult.fail(ROLE_NOT_MATCHED.getValue(), new HashMap<>());
        }

        if(sellerRepository.existsByEmail(dto.email())){
            return SignResult.fail(EMAIL_DUPLICATED.getValue(), new HashMap<>());
        }

        if(sellerRepository.existsByCompanyName(dto.companyName())){
            return SignResult.fail(COMPANY_NAME_DUPLICATED.getValue(), new HashMap<>());
        }

        return SignResult.success(NO_DUPLICATION.getValue(), new HashMap<>());
    }

    @Override
    public SignResult<Map<String,Object>> socialSignUp(String email, String provider) {  
        return null;
    }

    @Override
    public boolean supports(Role role) {
        return Role.SELLER.equals(role);
    }
}
