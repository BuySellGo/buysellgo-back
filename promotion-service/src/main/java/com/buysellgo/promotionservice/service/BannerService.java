package com.buysellgo.promotionservice.service;

import com.buysellgo.promotionservice.common.configs.AwsS3Config;
import com.buysellgo.promotionservice.dto.BannerRequestDto;
import com.buysellgo.promotionservice.entity.Banners;
import com.buysellgo.promotionservice.repository.BannersRepository;
import com.buysellgo.promotionservice.repository.PromotionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final PromotionRepository promotionRepository;
    private final AwsS3Config awsS3Config;


    public Banners createBanner(BannerRequestDto bannerRequestDto, MultipartFile bannerImagePath) throws IOException {

//        MultipartFile bannerImage = bannerRequestDto.getBannerImagePath();
        log.info("banner image multipartfile: {}", bannerImagePath.getOriginalFilename());

        String uniqueFileName
                = UUID.randomUUID() + "_" + bannerImagePath.getOriginalFilename();
        log.info("Saving uniqueFileName {}", uniqueFileName);

        String imagePath = awsS3Config.uploadToS3Bucket(bannerImagePath.getBytes(), uniqueFileName);
        log.info("Saving banner image to {}", imagePath);

        Banners saved = bannersRepository.save(
                bannerRequestDto.toEntity(imagePath, promotionRepository));

        return saved;

    }

    public Page<Banners.Vo> getBannerList(Pageable pageable) {
        Page<Banners> bannersPage = bannersRepository.findAll(pageable);

        if(bannersPage.isEmpty()){
            log.warn("No banner found for the given page request: {}", pageable);
            return Page.empty();
        }

        return bannersPage.map(banners -> {
            try {
                return banners.toVo();
            } catch (Exception e) {
                log.error("Error converting Banners to Vo: {}", banners);
                throw new RuntimeException(e);
            }
        });
    }

    public Banners.Vo activateBanner(Long bannerId, Boolean isActivated) {
        Banners banners = bannersRepository.findById(bannerId).orElseThrow(
                () -> new EntityNotFoundException("Banner not found with id: " + bannerId)
        );

        banners.setActivated(isActivated);

        Banners saved = bannersRepository.save(banners);

        return saved.toVo();
    }

    public Banners updateBanner(Long bannerId,
                                BannerRequestDto bannerRequestDto,
                                MultipartFile bannerImagePath) throws IOException {
        Banners banners = bannersRepository.findById(bannerId).orElseThrow(
                () -> new EntityNotFoundException("Banner not found with id: " + bannerId)
        );

        log.info("banner image multipartfile: {}", bannerImagePath.getOriginalFilename());

        String uniqueFileName
                = UUID.randomUUID() + "_" + bannerImagePath.getOriginalFilename();
        log.info("Saving uniqueFileName {}", uniqueFileName);

        String imagePath = awsS3Config.uploadToS3Bucket(bannerImagePath.getBytes(), uniqueFileName);
        log.info("Saving banner image to {}", imagePath);

//        Banners saved = bannersRepository.save(
//                bannerRequestDto.toEntity(imagePath, promotionRepository));

        banners.update(bannerRequestDto, banners.getPromotion(), imagePath);

        Banners saved = bannersRepository.save(banners);

        return saved;


    }

    public void deleteBanner(Long bannerId) {
        Banners banners = bannersRepository.findById(bannerId).orElseThrow(
                () -> new EntityNotFoundException("Banner not found with id: " + bannerId)
        );

        bannersRepository.delete(banners);
    }
}

/*
        // 비밀번호 확인하기 (암호화 되어있으니 encoder에게 부탁)
        if (!encoder.matches(dto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("NO_PW");
        }
 */