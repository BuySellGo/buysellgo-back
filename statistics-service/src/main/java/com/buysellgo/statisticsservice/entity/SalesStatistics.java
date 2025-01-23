package com.buysellgo.statisticsservice.entity;

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
@Table(name = "tbl_sales_statics")
public class SalesStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "sales_amount")
    private Long salesAmount;

    public static SalesStatistics of(Long sellerId, Timestamp createdAt, Long salesAmount) {
        return SalesStatistics.builder()
                .sellerId(sellerId)
                .createdAt(createdAt)
                .salesAmount(salesAmount)
                .build();
    }

    public Vo toVo(){
        return new Vo(id, sellerId, createdAt, salesAmount);
    }

    public record Vo(Long id,
                      Long sellerId,
                      Timestamp createdAt,
                      Long salesAmount) {
    }
}
