package com.buysellgo.paymentservice.entity;

import java.sql.Timestamp;

import com.buysellgo.paymentservice.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
@Builder
@Table(name = "tbl_pay_method")
public class PayMethod extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long payMethodId;

    @Column(name = "user_id", columnDefinition = "bigint", nullable = false, unique = false)
    private long userId;

    @Column(name = "pay_method_name", columnDefinition = "varchar(255)", nullable = false, unique = false)
    private String payMethodName;

    @Column(name = "pay_method_type", columnDefinition = "enum('CREDIT_CARD', 'KAKAO_PAY')", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private PayMethodType payMethodType;

    @Column(name = "pay_method_number", columnDefinition = "varchar(255)", nullable = false, unique = false)
    private String payMethodNumber;

    public static PayMethod of(long userId, String payMethodName, PayMethodType payMethodType, String payMethodNumber){
        return PayMethod.builder()
            .userId(userId)
            .payMethodName(payMethodName)
            .payMethodType(payMethodType)
            .payMethodNumber(payMethodNumber)
            .build();
    }

    public Vo toVo(){
        return new Vo(payMethodId, userId, payMethodName, payMethodType, payMethodNumber, version, createdAt, updatedAt);
    }

    public record Vo(
        long payMethodId,
        long userId,
        String payMethodName,
        PayMethodType payMethodType,
        String payMethodNumber,
        long version,
        Timestamp createdAt,
        Timestamp updatedAt
    ){}
}