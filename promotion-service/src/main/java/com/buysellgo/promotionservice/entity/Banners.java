package com.buysellgo.promotionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_banners")
public class Banners {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "banner_title", columnDefinition = "varchar(100)")
    private String bannerTitle;

    @Column(name= "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "image_path", columnDefinition = "varchar(200)")
    private String imagePath;

    @Column(name = "product_url", columnDefinition = "varchar(200)")
    private String productUrl;

    public static Banners of(String bannerTitle,
                             Timestamp startDate,
                             Timestamp endDate,
                             String imagePath,
                             String productUrl) {

        return Banners.builder()
//                .createdAt(LocalDateTime.now(ZoneId.of("UTC")))
                .createdAt(Timestamp.from(Instant.now()))
                .bannerTitle(bannerTitle)
                .startDate(startDate)
                .endDate(endDate)
                .imagePath(imagePath)
                .productUrl(productUrl)
                .build();
    }

    public Vo toVo(){
        return new Vo(id, createdAt, bannerTitle, startDate, endDate, imagePath, productUrl);
    }


    private record Vo(Long id,
                      Timestamp createdAt,
                      String bannerTitle,
                      Timestamp startDate,
                      Timestamp endDate,
                      String imageUrl,
                      String productUrl) {
    }
}
