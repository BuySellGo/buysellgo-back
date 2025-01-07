package com.buysellgo.userservice.strategy.sign;

import com.buysellgo.userservice.domain.user.LoginType;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.service.dto.SignUpDto;
import com.buysellgo.userservice.service.dto.UserSignUpDto;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.common.SignStrategy;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.buysellgo.userservice.repository.UserRepository;
import com.buysellgo.userservice.common.entity.Role;

@Component
@Transactional
@RequiredArgsConstructor    
public class UserSignStrategy implements SignStrategy<User.Vo> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignResult<User.Vo> signUp(SignUpDto dto) {
        if (!(dto instanceof UserSignUpDto userDto)) {
            return SignResult.failure("Invalid DTO type for user sign up");
        }

        try {
            return SignResult.success(
                userRepository.save(
                    User.of(
                        userDto.email(), 
                        passwordEncoder.encode(userDto.password()),
                        userDto.username(),
                        userDto.phone(),
                        LoginType.COMMON,
                        Role.USER,
                        userDto.emailCertified(), 
                        userDto.agreePICU(),
                        userDto.agreeEmail(), 
                        userDto.agreeTOS()
                    )
                ).toVo()
            );
        } catch (Exception e) {
            return SignResult.failure(e.getMessage());
        }
    }

    @Override
    public SignResult<Void> withdraw(String token) {
        // 회원 탈퇴 로직 구현
        return SignResult.failure("Not implemented");
    }

    @Override
    public boolean supports(Role role) {
        return Role.USER.equals(role);
    }
}
