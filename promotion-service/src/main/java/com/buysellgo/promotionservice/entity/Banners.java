package com.buysellgo.promotionservice.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private LocalDateTime createdAt;

    @Column(name = "banner_title", columnDefinition = "varchar(100)")
    private String bannerTitle;

    @Column(name= "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "image_url", columnDefinition = "varchar(200)")
    private String imageUrl;

    @Column(name = "product_url", columnDefinition = "varchar(200)")
    private String productUrl;

    public static Banners of(String bannerTitle,
                             LocalDateTime startDate,
                             LocalDateTime endDate,
                             String imageUrl,
                             String productUrl) {

        return Banners.builder()
                .createdAt(LocalDateTime.now(ZoneId.of("UTC")))
                .bannerTitle(bannerTitle)
                .startDate(startDate)
                .endDate(endDate)
                .imageUrl(imageUrl)
                .productUrl(productUrl)
                .build();
    }

    public Vo toVo(){
        return new Vo(id, createdAt, bannerTitle, startDate, endDate, imageUrl, productUrl);
    }


    private record Vo(Long id,
                      LocalDateTime createdAt,
                      String bannerTitle,
                      LocalDateTime startDate,
                      LocalDateTime endDate,
                      String imageUrl,
                      String productUrl) {
    }
}
