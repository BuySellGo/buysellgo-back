package com.buysellgo.userservice.user.service;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.user.controller.dto.UserCreateReq;
import com.buysellgo.userservice.user.domain.user.LoginType;
import com.buysellgo.userservice.user.domain.user.User;
import com.buysellgo.userservice.user.repository.UserRepository;
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
    private final PasswordEncoder passwordEncoder;

    public User.Vo userSign(UserCreateReq req) {
        userRepository.save(User.of(req.email(), passwordEncoder.encode(req.password()),
                req.username(), req.phone(),
                LoginType.COMMON, Role.USER,
                req.emailCertified(), req.agreePICU(),
                req.agreeEmail(), req.agreeTOS()));
        User user = userRepository.findByUsername(req.username()).orElseThrow();
        return user.toVo();
    }
}
