package com.buysellgo.promotionservice.service;

import com.buysellgo.promotionservice.common.configs.AwsS3Config;
import com.buysellgo.promotionservice.dto.BannerRequestDto;
import com.buysellgo.promotionservice.entity.Banners;
import com.buysellgo.promotionservice.repository.BannersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BannerService {
    private final BannersRepository bannersRepository;
    private final AwsS3Config awsS3Config;


    public Banners createBanner(BannerRequestDto bannerRequestDto, MultipartFile bannerImagePath) throws IOException {

//        MultipartFile bannerImage = bannerRequestDto.getBannerImagePath();
        log.info("banner image multipartfile {}", bannerImagePath.getOriginalFilename());

        String uniqueFileName
                = UUID.randomUUID() + "_" + bannerImagePath.getOriginalFilename();
        log.info("Saving uniqueFileName {}", uniqueFileName);

        String imagePath = awsS3Config.uploadToS3Bucket(bannerImagePath.getBytes(), uniqueFileName);
        log.info("Saving banner image to {}", imagePath);

        Banners saved = bannersRepository.save(bannerRequestDto.toEntity(imagePath));

        return saved;

    }
}

/*
        // 비밀번호 확인하기 (암호화 되어있으니 encoder에게 부탁)
        if (!encoder.matches(dto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("NO_PW");
        }
 */