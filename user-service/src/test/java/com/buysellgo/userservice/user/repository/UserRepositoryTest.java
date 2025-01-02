package com.buysellgo.userservice.user.repository;

import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.user.domain.user.AcountStatus;
import com.buysellgo.userservice.user.domain.user.LoginType;
import com.buysellgo.userservice.user.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("junit")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("user를 생성해본다.")
    void createUser() {
        userRepository.save(User.of("test@test.com", "password", "test","010-1234-5678",
                LoginType.COMMON, Role.USER, true, true, true, true));
        assertNotNull(userRepository.findByUsername("test"));
    }

    @Test
    @DisplayName("user를 vo로 조회한다.")
    void getUser() {
        createUser();
        User test = userRepository.findByUsername("test").orElseThrow();
        System.out.println(test.toVo());
        assertNotNull(test);
    }

    @Test
    @DisplayName("user를 수정해본다.")
    void updateUser() {
        createUser();
        User test = userRepository.findByUsername("test").orElseThrow();
        test.setStatus(AcountStatus.DISABLE);
        userRepository.save(test);
        User test1 = userRepository.findByUsername("test").orElseThrow();
        System.out.println(test1.toVo());
        assertEquals(AcountStatus.DISABLE, test1.getStatus());
    }

    @Test
    @DisplayName("user를 삭제해본다.")
    void deleteUser() {
        createUser();
        User test = userRepository.findByUsername("test").orElseThrow();
        userRepository.delete(test);
        assertEquals(0, userRepository.findAll().size());
    }
}