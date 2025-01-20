package com.buysellgo.promotionservice.dto;

import com.buysellgo.promotionservice.entity.Banners;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerRequestDto {

    @Schema(title = "배너 제목", example = "50% 세일", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "배너 제목은 필수 입니다.")
    private String bannerTitle;

    @Schema(title = "배너 URL", example = "50% 세일", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "배너를 클릭했을때 이동하는 URL 은 필수 입니다.")
    private String bannerUrl;

    @Schema(title = "공지 시작 시간", example="2025-01-15T01:55:34.756+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "시작 시간은 필수 입니다.")
    private Timestamp startDate;

    @Schema(title = "공지 종료 시간", example="2025-01-18T01:55:34.756+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "종료 시간은 필수 입니다.")
    private Timestamp endDate;

//    @Schema(title = "배너 이미지 Path", example = "/Users/playdata2/Pictures/banner.webp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
////    @NotNull(message = "배너 이미지 Path 는 필수 입니다.")
//    private MultipartFile bannerImagePath;

    public Banners toEntity(String bannerImgPath) {
        return Banners.of(bannerTitle, startDate, endDate, bannerImgPath, bannerUrl);
    }


}
