package com.buysellgo.userservice.domain.seller;

import com.buysellgo.userservice.common.entity.Address;
import com.buysellgo.userservice.common.entity.Authorization;
import com.buysellgo.userservice.common.entity.BaseEntity;
import com.buysellgo.userservice.common.entity.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_seller")
public class Seller extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id",columnDefinition = "bigint")
    private long sellerId;

    @Column(name = "company_name",columnDefinition = "varchar(50)",nullable = false, unique = true)
    private String companyName;

    @Column(name = "president_name",columnDefinition = "varchar(50)",nullable = false)
    private String presidentName;

    @Embedded
    private Address address;

    @Column(name = "role", columnDefinition = "enum('SELLER')", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "email",columnDefinition = "varchar(50)",nullable = false, unique = true)
    private String email;

    @Column(name = "password",columnDefinition = "varchar(200)",nullable = false)
    private String password;

    @Column(name = "business_registration_number",columnDefinition = "varchar(30)",nullable = false, unique = true)
    private String businessRegistrationNumber;

    @Column(name = "business_registration_number_img",columnDefinition = "varchar(100)",nullable = false)
    private String businessRegistrationNumberImg;

    @Column(name = "is_approved",columnDefinition = "enum('AUTHORIZED','UNAUTHORIZED')",nullable = false)
    @Enumerated(EnumType.STRING)
    private Authorization isApproved;

    public static Seller of(String companyName, String presidentName, Address address, String email,Role role, String encodePassword, String businessRegistrationNumber, String businessRegistrationNumberImg) {
        return Seller.builder()
                .companyName(companyName)
                .presidentName(presidentName)
                .address(address)
                .email(email)
                .role(role)
                .password(encodePassword)
                .businessRegistrationNumber(businessRegistrationNumber)
                .businessRegistrationNumberImg(businessRegistrationNumberImg)
                .isApproved(Authorization.UNAUTHORIZED)
                .build();
    }

    public Vo toVo(){return new Vo(sellerId, companyName, presidentName, address,
                email, role.toString(), businessRegistrationNumber,
                businessRegistrationNumberImg, isApproved.toString());

    }

    public record Vo(
            long sellerId
            , String companyName
            , String presidentName
            , Address address
            , String email
            , String role
            , String businessRegistrationNumber
            , String businessRegistrationNumberImg
            , String isApproved
    ){}
}
