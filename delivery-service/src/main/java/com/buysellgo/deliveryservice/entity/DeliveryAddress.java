package com.buysellgo.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_delivery_address")
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "address_name", length = 50, columnDefinition = "VARCHAR(50) DEFAULT NULL", nullable = true)
    private String addressName;

    @Column(name = "receiver", length = 50, columnDefinition = "VARCHAR(50) DEFAULT NULL", nullable = true)
    private String receiver;

    @Column(name = "phone", length = 30, columnDefinition = "VARCHAR(30) DEFAULT NULL", nullable = true)
    private String phone;

    @Column(name = "zip_code", length = 10, columnDefinition = "VARCHAR(10) DEFAULT NULL", nullable = true)
    private String zipCode;

    @Column(name = "address", length = 100, columnDefinition = "VARCHAR(100) DEFAULT NULL", nullable = true)
    private String address;

    @Column(name = "detail_address", length = 100, columnDefinition = "VARCHAR(100) DEFAULT NULL", nullable = true)
    private String detailAddress;

    @Column(name = "is_default", length = 1, columnDefinition = "CHAR(1) DEFAULT NULL", nullable = true)
    private Boolean isDefault;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT NULL", nullable = true)
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME DEFAULT NULL", nullable = true)
    private Timestamp updatedAt;

    // of 메서드 (빌더 패턴 사용)
    public static DeliveryAddress of(
            Long userId,
            String addressName,
            String receiver,
            String phone,
            String zipCode,
            String address,
            String detailAddress,
            Boolean isDefault,
            Timestamp createdAt) {

        return DeliveryAddress.builder()
                .userId(userId)
                .addressName(addressName)
                .receiver(receiver)
                .phone(phone)
                .zipCode(zipCode)
                .address(address)
                .detailAddress(detailAddress)
                .isDefault(isDefault)
                .createdAt(createdAt)
                .updatedAt(Timestamp.from(Instant.now()))
                .build();
    }

    // toVo 메서드 (Address 엔티티를 AddressVo로 변환)
    public DeliveryAddressVo toVo() {
        return new DeliveryAddressVo(
                id,
                userId,
                addressName,
                receiver,
                phone,
                zipCode,
                address,
                detailAddress,
                isDefault,
                createdAt,
                updatedAt);
    }

    // recordVo 메서드 (Address 엔티티를 AddressRecordVo로 변환, 일부 필드만 포함)
    private record DeliveryAddressVo(
            Long id,
            Long userId,
            String addressName,
            String receiver,
            String phone,
            String zipCode,
            String address,
            String detailAddress,
            Boolean isDefault,
            Timestamp createdAt,
            Timestamp updatedAt) {
    }

}

/*
    "userId":"2",
    "addressName":"집",
    "receiver":"본인",
    "phone":"010-1234-1234",
    "zipCode:"12345",
    "address:"서울시 강남구 강남로 123",
    "detailAddress:"101동 200호",
    "isDefault:"true",
    "createdAt:"{{current-time}}"
 */