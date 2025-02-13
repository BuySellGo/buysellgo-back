package com.buysellgo.userservice.strategy.info.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.buysellgo.userservice.controller.info.dto.InfoUpdateReq;
import com.buysellgo.userservice.controller.info.dto.UserUpdateReq;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.repository.ProfileRepository;
import com.buysellgo.userservice.repository.UserRepository;
import org.springframework.stereotype.Component;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.strategy.info.common.InfoResult;
import com.buysellgo.userservice.strategy.info.common.InfoStrategy;
    
import lombok.RequiredArgsConstructor;

import static com.buysellgo.userservice.util.CommonConstant.*;
import com.buysellgo.userservice.domain.user.Profile;

import org.apache.commons.lang3.ObjectUtils;    
import org.springframework.security.crypto.password.PasswordEncoder;    
                

@Component
@RequiredArgsConstructor
public class UserInfoStrategy implements InfoStrategy<Map<String, Object>> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    @Override
    public InfoResult<Map<String, Object>> getOne(String email) {
        Map<String, Object> data = new HashMap<>();
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()) {
            return InfoResult.fail(USER_NOT_FOUND.getValue(),data);
        }
        User user = userOptional.get();
        Profile profile = profileRepository.findByUser(user).orElseThrow();
        data.put(USER_VO.getValue(),user.toVo());
        data.put(PROFILE_VO.getValue(),profile.toVo());
        return InfoResult.success(SUCCESS.getValue(),data);
    }

    @Override
    public InfoResult<Map<String, Object>> getList(Role role) {
        return null;
    }

    @Override
    public InfoResult<Map<String, Object>> edit(InfoUpdateReq req, String email) {
        Map<String, Object> data = new HashMap<>();
        UserUpdateReq userReq = (UserUpdateReq) req;
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return InfoResult.fail(USER_NOT_FOUND.getValue(), data);
        }
        User user = userOptional.get();
        Profile profile = profileRepository.findByUser(user).orElseThrow();

        switch (userReq.getType()) {
            case INFO ->{
                handleInfoUpdate(userReq, user, data);
                if(data.isEmpty()){
                    return InfoResult.fail(NO_CHANGE.getValue(), data);
                }
            }
            case PROFILE -> {
                handleProfileUpdate(userReq, profile, data);
                if(data.isEmpty()){
                    return InfoResult.fail(NO_CHANGE.getValue(), data);
                }
            }
            case PASSWORD -> {
                handlePasswordUpdate(userReq, user, data);
                if(data.isEmpty()){
                    return InfoResult.fail(NO_CHANGE.getValue(), data);
                }
            }
            default -> {
                return InfoResult.fail("잘못된 요청입니다.", data);
            }
        }
        return InfoResult.success(UPDATE_SUCCESS.getValue(), data);
    }

    private void handleInfoUpdate(UserUpdateReq userReq, User user, Map<String, Object> data) {
        if (ObjectUtils.isNotEmpty(userReq.getUsername())) {
            user.setUsername(userReq.getUsername());
            data.put("username", user.getUsername());
        }
        if (ObjectUtils.isNotEmpty(userReq.getPhone())) {
            user.setPhone(userReq.getPhone());
            data.put("phone", user.getPhone());
        }
        if (!data.isEmpty()) {
            userRepository.save(user);
            data.put(USER_VO.getValue(), user.toVo());
        }
    }

    private void handleProfileUpdate(UserUpdateReq userReq, Profile profile, Map<String, Object> data) {
        if (ObjectUtils.isNotEmpty(userReq.getProfileImageUrl())) {
            profile.setProfileImage(userReq.getProfileImageUrl());
            data.put("profileImageUrl", profile.getProfileImage());
        }
        if (ObjectUtils.isNotEmpty(userReq.getAges())) {
            profile.setAges(userReq.getAges());
            data.put("ages", profile.getAges());
        }
        if (ObjectUtils.isNotEmpty(userReq.getGender())) {
            profile.setGender(userReq.getGender());
            data.put("gender", profile.getGender());
        }
        if (ObjectUtils.isNotEmpty(userReq.getBirthday())) {
            profile.setBirthday(userReq.getBirthday());
            data.put("birthday", profile.getBirthday());
        }
        if (!data.isEmpty()) {
            profileRepository.save(profile);
            data.put(PROFILE_VO.getValue(), profile.toVo());
        }
    }

    private void handlePasswordUpdate(UserUpdateReq userReq, User user, Map<String, Object> data) {
        if (ObjectUtils.isNotEmpty(userReq.getPassword())) {
            user.setPassword(passwordEncoder.encode(userReq.getPassword()));
            userRepository.save(user);
            data.put(USER_VO.getValue(), user.toVo());
        }
    }

    @Override
    public boolean supports(Role role) {
        return role == Role.USER;
    }
}
