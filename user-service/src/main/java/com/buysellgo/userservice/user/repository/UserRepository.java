package com.buysellgo.userservice.user.repository;

import com.buysellgo.userservice.user.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String test);

    Optional<User> findByEmail(String email);
}
