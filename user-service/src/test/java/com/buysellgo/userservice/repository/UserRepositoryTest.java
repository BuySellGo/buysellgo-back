package com.buysellgo.userservice.repository;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.common.entity.Authorization;
import com.buysellgo.userservice.domain.user.LoginType;
import com.buysellgo.userservice.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("user를 생성해본다.")
    void createUser() {
        userRepository.save(User.of("test@test.com", "testPassword",
                "testUsername","testPhone",
                LoginType.COMMON, Role.USER, true, true, true, true));
        assertEquals("testUsername",userRepository.findByUsername("testUsername").orElseThrow().getUsername());
    }

    @Test
    @DisplayName("user를 vo로 조회한다.")
    void getUser() {
        createUser();
        User test = userRepository.findByUsername("testUsername").orElseThrow();
        System.out.println(test.toVo());
        assertEquals("testUsername",test.toVo().username());
    }

    @Test
    @DisplayName("user를 수정해본다.")
    void updateUser() {
        createUser();
        User test = userRepository.findByUsername("testUsername").orElseThrow();
        test.setStatus(Authorization.UNAUTHORIZED);
        userRepository.save(test);
        User test1 = userRepository.findByUsername("testUsername").orElseThrow();
        System.out.println(test1.toVo());
        assertEquals(Authorization.UNAUTHORIZED, test1.getStatus());
    }

    @Test
    @DisplayName("user를 삭제해본다.")
    void deleteUser() {
        createUser();
        User test = userRepository.findByUsername("testUsername").orElseThrow();
        userRepository.delete(test);
        assertEquals(0, userRepository.findAll().size());
    }
}