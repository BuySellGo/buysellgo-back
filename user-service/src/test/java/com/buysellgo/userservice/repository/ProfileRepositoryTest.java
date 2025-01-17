package com.buysellgo.userservice.repository;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();
        userRepository.deleteAll();
        testUser = userRepository.save(User.of("test@test.com", "testPassword",
                "testUsername", "testPhone",
                LoginType.COMMON, Role.USER, true, true, true, true));
    }

    @Test
    @DisplayName("profile을 생성해본다.")
    void createProfile() {
        Profile profile = Profile.of(testUser);
        profileRepository.save(profile);
        assertEquals("0", profileRepository.findByUser(testUser).orElseThrow().getAges());
    }

    @Test
    @DisplayName("profile을 vo로 조회한다.")
    void getProfile() {
        createProfile();
        Profile profile = profileRepository.findByUser(testUser).orElseThrow();
        System.out.println(profile.toVo());
        assertEquals("0", profile.toVo().ages());
    }

    @Test
    @DisplayName("profile을 수정해본다.")
    void updateProfile() {
        createProfile();
        Profile profile = profileRepository.findByUser(testUser).orElseThrow();
        profile.setAges("30");
        profileRepository.save(profile);
        Profile updatedProfile = profileRepository.findByUser(testUser).orElseThrow();
        System.out.println(updatedProfile.toVo());
        assertEquals("30", updatedProfile.getAges());
    }

    @Test
    @DisplayName("profile을 삭제해본다.")
    void deleteProfile() {
        createProfile();
        Profile profile = profileRepository.findByUser(testUser).orElseThrow();
        profileRepository.delete(profile);
        assertEquals(0, profileRepository.findAll().size());
    }
}