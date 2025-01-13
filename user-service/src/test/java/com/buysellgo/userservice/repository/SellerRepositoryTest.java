package com.buysellgo.userservice.repository;

import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.common.entity.Authorization;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.domain.seller.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SellerRepositoryTest {

    @Autowired
    private SellerRepository sellerRepository;

    @BeforeEach
    void setUp() {
        sellerRepository.deleteAll();
    }

    @Test
    @DisplayName("seller를 생성한다.")
    void createSeller() {
        sellerRepository.save(Seller.of("testCompany","testPresident"
                , Address.builder().city("testCity").street("testStreet").zipCode("testZipCode").build(),
                "test@test.com", Role.SELLER,"testPassword", "testBRN",
                "testBRNI"));
        assertEquals("testCompany", sellerRepository.findByCompanyName("testCompany").orElseThrow().getCompanyName());
    }

    @Test
    @DisplayName("seller를 Vo로 조회한다.")
    void getSeller() {
        createSeller();
        Seller test = sellerRepository.findByCompanyName("testCompany").orElseThrow();
        System.out.println(test.toVo());
        assertEquals("testCompany",test.toVo().companyName());
    }

    @Test
    @DisplayName("seller를 수정한다.")
    void updateSeller() {
        createSeller();
        Seller test = sellerRepository.findByCompanyName("testCompany").orElseThrow();
        test.setIsApproved(Authorization.AUTHORIZED);
        sellerRepository.save(test);
        Seller test1 = sellerRepository.findByCompanyName("testCompany").orElseThrow();
        System.out.println(test1.toVo());
        assertEquals(Authorization.AUTHORIZED, test1.getIsApproved());
    }

    @Test
    @DisplayName("seller를 삭제한다.")
    void deleteSeller() {
        createSeller();
        Seller test = sellerRepository.findByCompanyName("testCompany").orElseThrow();
        sellerRepository.delete(test);
        assertEquals(0, sellerRepository.findAll().size());

    }




}