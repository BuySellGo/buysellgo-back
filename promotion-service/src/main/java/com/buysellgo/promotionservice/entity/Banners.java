package com.buysellgo.promotionservice.entity;

import com.buysellgo.promotionservice.dto.BannerRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    @ToString.Exclude
    private Promotion promotion;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "banner_title", columnDefinition = "varchar(100)")
    private String bannerTitle;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "image_path", columnDefinition = "varchar(200)")
    private String imagePath;

    @Column(name = "product_url", columnDefinition = "varchar(200)")
    private String productUrl;

    @Column(name = "is_activated")
    private Boolean isActivated;

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public static Banners of(Promotion promotion,
                             String bannerTitle,
                             Timestamp startDate,
                             Timestamp endDate,
                             String imagePath,
                             String productUrl,
                             Boolean isActivated) {

        return Banners.builder()
                .promotion(promotion)
                .createdAt(Timestamp.from(Instant.now()))
                .bannerTitle(bannerTitle)
                .startDate(startDate)
                .endDate(endDate)
                .imagePath(imagePath)
                .productUrl(productUrl)
                .isActivated(isActivated)
                .build();
    }

    public Vo toVo() {
        return new Vo(id, promotion.getId(), createdAt, bannerTitle, startDate, endDate, imagePath, productUrl, isActivated);
    }


    public record Vo(Long id,
                     Long promotionId,
                     Timestamp createdAt,
                     String bannerTitle,
                     Timestamp startDate,
                     Timestamp endDate,
                     String imagePath,
                     String productUrl,
                     Boolean isActivated) {
    }

    public void update(BannerRequestDto bannerRequestDto, Promotion promotion, String imageFilePath) {

        this.promotion = promotion;
        this.bannerTitle = bannerRequestDto.getBannerTitle();
        this.startDate = bannerRequestDto.getStartDate();
        this.endDate = bannerRequestDto.getEndDate();
        this.imagePath = imageFilePath;
        this.productUrl = bannerRequestDto.getProductUrl();
        this.isActivated = bannerRequestDto.getIsActivated();

    }
}
