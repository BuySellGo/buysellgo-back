package com.buysellgo.userservice.service;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.controller.dto.SellerCreateReq;
import com.buysellgo.userservice.controller.dto.UserCreateReq;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.domain.user.LoginType;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.SellerRepository;
import com.buysellgo.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SignService {
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public User.Vo userSign(UserCreateReq req) {
        userRepository.save(User.of(req.email(), passwordEncoder.encode(req.password()),
                req.username(), req.phone(),
                LoginType.COMMON, Role.USER,
                req.emailCertified(), req.agreePICU(),
                req.agreeEmail(), req.agreeTOS()));
        User user = userRepository.findByUsername(req.username()).orElseThrow();
        return user.toVo();
    }

    public Seller.Vo sellerSign(SellerCreateReq req) {
        sellerRepository.save(Seller.of(req.companyName(), req.presidentName(),
                req.address(),req.email(),
                passwordEncoder.encode(req.password()), req.businessRegistrationNumber(),
                req.businessRegistrationNumberImg()));
        Seller seller = sellerRepository.findByCompanyName(req.companyName()).orElseThrow();
        return seller.toVo();
    }

    public void userDelete(String token) {
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        User user = userRepository.findByEmail(userInfo.getEmail()).orElseThrow();
        userRepository.delete(user);
    }
}
