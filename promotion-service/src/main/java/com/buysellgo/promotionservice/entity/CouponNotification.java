package com.buysellgo.promotionservice.entity;

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
@Table(name = "tbl_coupon_notification")
public class CouponNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

//    @Column(name = "profile_id")
//    private Long profileId;
//
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "noti_content", columnDefinition = "varchar(500)")
    private String notiContent;

    @Column(name = "noti_datetime")
    private Timestamp notiDatetime;

    @Column(name = "noti_read_datetime")
    private Timestamp notiReadDateTime;

    public static CouponNotification of(Long userId,
                                        String notiContent,
                                        Timestamp notiDatetime){
//                                        Timestamp notiReadDateTime) {
        return CouponNotification.builder()
                .userId(userId)
                .createdAt(Timestamp.from(Instant.now()))
                .notiContent(notiContent)
                .notiDatetime(notiDatetime)
//                .notiReadDateTime(notiReadDateTime)
                .build();
    }

    private Vo toVo(){
        return new Vo(id, userId, notiContent, notiDatetime, notiReadDateTime);
    }

    private record Vo(Long id,
                      Long userId,
                      String notiContent,
                      Timestamp notiDatetime,
                      Timestamp notiReadDateTime) {
    }
}
