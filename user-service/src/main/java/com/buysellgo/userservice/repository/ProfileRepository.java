package com.buysellgo.userservice.repository;

import com.buysellgo.userservice.domain.user.Profile;
import com.buysellgo.userservice.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User testUser);
}
