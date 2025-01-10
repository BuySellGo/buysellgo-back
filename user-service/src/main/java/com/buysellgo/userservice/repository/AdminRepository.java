package com.buysellgo.userservice.repository;

import org.springframework.stereotype.Repository;
import com.buysellgo.userservice.domain.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{
    boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);
}

