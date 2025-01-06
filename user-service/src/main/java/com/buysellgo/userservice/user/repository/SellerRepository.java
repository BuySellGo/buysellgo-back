package com.buysellgo.userservice.user.repository;

import com.buysellgo.userservice.user.domain.seller.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByCompanyName(String testCompany);

    Optional<Seller> findByEmail(String email);
}
