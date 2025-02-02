package com.buysellgo.qnaservice.common.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 타 엔터티에서 사용 가능한 형태로 만드는 아노테이션
@Embeddable
@Getter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    private String city;
    private String street;
    private String zipCode; // 05383

}
