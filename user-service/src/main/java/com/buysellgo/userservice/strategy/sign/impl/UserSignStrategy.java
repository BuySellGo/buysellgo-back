package com.buysellgo.userservice.strategy.sign.impl;

import com.buysellgo.userservice.common.auth.JwtTokenProvider;
import com.buysellgo.userservice.common.auth.TokenUserInfo;
import com.buysellgo.userservice.common.entity.Authorization;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.user.LoginType;
import com.buysellgo.userservice.domain.user.Profile;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.ProfileRepository;
import com.buysellgo.userservice.repository.UserRepository;
import com.buysellgo.userservice.strategy.sign.common.SignResult;
import com.buysellgo.userservice.strategy.sign.common.SignStrategy;
import com.buysellgo.userservice.strategy.sign.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.buysellgo.userservice.util.CommonConstant.*;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserSignStrategy implements SignStrategy<Map<String,Object>> {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> userTemplate;
    private final ProfileRepository profileRepository;

    @Override
    public SignResult<Map<String,Object>> signUp(SignUpDto dto) {
        Map<String, Object> data = new HashMap<>();
        try {
            if (!(dto instanceof UserSignUpDto userSignUpDto)) {
                return SignResult.fail(DTO_NOT_MATCHED.getValue(), data);
            }

            User user = User.of(userSignUpDto.email(),
                    passwordEncoder.encode(userSignUpDto.password()),
                    userSignUpDto.username(),
                    userSignUpDto.phone(),
                    LoginType.COMMON,
                    Role.USER,
                    userSignUpDto.emailCertified(),
                    userSignUpDto.agreePICU(),
                    userSignUpDto.agreeEmail(),
                    userSignUpDto.agreeTOS()
            );
            data.put(USER_VO.getValue(), user.toVo());

            if(userRepository.existsByEmail(user.getEmail()) ||
                    userRepository.existsByUsername(user.getUsername()) ||
                    userRepository.existsByPhone(user.getPhone())){
                return SignResult.fail(VALUE_DUPLICATED.getValue(),data);
            }
            userRepository.save(user);
            Profile profile = Profile.of(user);

            profileRepository.save(profile);
            data.put(PROFILE_VO.getValue(), profile.toVo());
            return SignResult.success(USER_CREATED.getValue(), data);
        } catch (RuntimeException e) {
            e.setStackTrace(e.getStackTrace());
            return SignResult.fail(SAVE_FAILURE.getValue(), data);
        }
    }

    @Override
    public SignResult<Map<String,Object>> withdraw(String token) {
        Map<String,Object> data = new HashMap<>();
        TokenUserInfo userInfo = jwtTokenProvider.validateAndGetTokenUserInfo(token);
        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());

        if (userOptional.isEmpty()) {
            return SignResult.fail(USER_NOT_FOUND.getValue(),data);
        }
        User user = userOptional.get();
        data.put(USER_VO.getValue(), user.toVo());

        try{
            userRepository.delete(user);
            userTemplate.delete(userInfo.getEmail());
        } catch (Exception e) {
           return SignResult.fail(e.getMessage(),data);
        }

        return SignResult.success(USER_DELETED.getValue(),data);
    }

    @Override
    public SignResult<Map<String, Object>> activate(ActivateDto dto) {
        if(!dto.role().equals(Role.USER)){
            return SignResult.fail(ROLE_NOT_MATCHED.getValue(), new HashMap<>());
        }

        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        if(userOptional.isEmpty()){
            return SignResult.fail(USER_NOT_FOUND.getValue(), new HashMap<>());
        }
        User user = userOptional.get();
        user.setStatus(Authorization.AUTHORIZED);
        userRepository.save(user);

        return SignResult.success(USER_ACTIVATED.getValue(), new HashMap<>());
    }

    @Override
    public SignResult<Map<String, Object>> deactivate(ActivateDto dto) {
        if(!dto.role().equals(Role.USER)){
            return SignResult.fail(ROLE_NOT_MATCHED.getValue(), new HashMap<>());
        }

        Optional<User> userOptional = userRepository.findByEmail(dto.email());
        if(userOptional.isEmpty()){
            return SignResult.fail(USER_NOT_FOUND.getValue(), new HashMap<>());
        }
        User user = userOptional.get();
        user.setStatus(Authorization.UNAUTHORIZED);
        userRepository.save(user);

        return SignResult.success(USER_DEACTIVATED.getValue(), new HashMap<>());
    }

    @Override
    public SignResult<Map<String, Object>> duplicate(DuplicateDto dto) {
        if (!dto.role().equals(Role.USER)) {
            return SignResult.fail(ROLE_NOT_MATCHED.getValue(), new HashMap<>());
        }

        if (StringUtils.isNotEmpty(dto.email()) && userRepository.existsByEmail(dto.email())) {
            return SignResult.fail(EMAIL_DUPLICATED.getValue(), new HashMap<>());
        }

        if (StringUtils.isNotEmpty(dto.username()) && userRepository.existsByUsername(dto.username())) {
            return SignResult.fail(USERNAME_DUPLICATED.getValue(), new HashMap<>());
        }

        return SignResult.success(NO_DUPLICATION.getValue(), new HashMap<>());
    }

    @Override
    public SignResult<Map<String, Object>> socialSignUp(String email, String provider) {
        Map<String, Object> data = new HashMap<>();
        User user = User.of(email, passwordEncoder.encode(UUID.randomUUID().toString()),
        email, 
        "000-0000-0000", LoginType.valueOf(provider.toUpperCase()), Role.USER, true, true, true, true);
        userRepository.save(user);
        Profile profile = Profile.of(user);
        profileRepository.save(profile);
        data.put(USER_VO.getValue(), user.toVo());
        data.put(PROFILE_VO.getValue(), profile.toVo());
        return SignResult.success(USER_CREATED.getValue(), data);
    }

    @Override
    public boolean supports(Role role) {
        return Role.USER.equals(role);
    }
}
