package com.buysellgo.statisticsservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_access_statistics")
public class AccessStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_statistics_id")
    private Long accessStatisticsId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "access_ip")
    private String accessIp;

    @Column(name = "access_date_time")
    private Timestamp accessDateTime;

    public static AccessStatistics of(Long userId, String accessIp) {
        return AccessStatistics.builder()
                .userId(userId)
                .accessIp(accessIp)
                .accessDateTime(Timestamp.from(Instant.now()))
                .build();
    }

    public Vo toVo(){
        return new Vo(accessStatisticsId, userId, accessIp, accessDateTime);
    }

    private record Vo(Long accessStatisticsId,
                      Long userId,
                      String accessIp,
                      Timestamp accessDateTime) {
    }
}
