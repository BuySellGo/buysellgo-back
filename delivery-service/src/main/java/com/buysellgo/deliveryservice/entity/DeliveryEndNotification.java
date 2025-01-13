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
@Table(name = "tbl_delivery_end_notification")
public class DeliveryEndNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "noti_content", columnDefinition = "varchar(500)")
    private String notiContent;

    @Column(name = "noti_datetime")
    private Timestamp notiDatetime;

    @Column(name = "noti_read_datetime")
    private Timestamp notiReadDateTime;

    public static DeliveryEndNotification of(Long profileId,
                                             String notiContent,
                                             Timestamp notiDatetime,
                                             Timestamp notiReadDateTime) {
        return DeliveryEndNotification.builder()
                .profileId(profileId)
                .createdAt(Timestamp.from(Instant.now()))
                .notiContent(notiContent)
                .notiDatetime(notiDatetime)
                .notiReadDateTime(notiReadDateTime)
                .build();
    }

    private Vo toVo(){
        return new Vo(id, profileId, notiContent, notiDatetime, notiReadDateTime);
    }

    private record Vo(Long id,
                      Long profileId,
                      String notiContent,
                      Timestamp notiDatetime,
                      Timestamp notiReadDateTime) {
    }

}
