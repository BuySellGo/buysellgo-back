package com.buysellgo.userservice.strategy.sign;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.service.dto.SignUpDto;
import com.buysellgo.userservice.service.dto.SellerSignUpDto;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.common.SignStrategy;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class SellerSignStrategy implements SignStrategy<Seller.Vo> {
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public SignResult<Seller.Vo> signUp(SignUpDto dto) {
        if (!(dto instanceof SellerSignUpDto sellerDto)) {
            return SignResult.failure("Invalid DTO type for seller sign up");
        }

        try {
            return SignResult.success(
                sellerRepository.save(
                    Seller.of(
                        sellerDto.companyName(), 
                        sellerDto.presidentName(),
                        sellerDto.address(), 
                        sellerDto.email(),
                        passwordEncoder.encode(sellerDto.password()), 
                        sellerDto.businessRegistrationNumber(),
                        sellerDto.businessRegistrationNumberImg()
                    )
                ).toVo()
            );
        } catch (Exception e) {
            return SignResult.failure(e.getMessage());
        }
    }

    @Override
    public SignResult<Void> withdraw(String token) {
        // 판매자 탈퇴 로직 구현
        return SignResult.failure("Not implemented");
    }

    @Override
    public boolean supports(Role role) {
        return Role.SELLER.equals(role);
    }
}
