package com.buysellgo.promotionservice.service;

import com.buysellgo.promotionservice.dto.PromotionRequestDto;
import com.buysellgo.promotionservice.entity.Promotion;
import com.buysellgo.promotionservice.repository.PromotionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public Promotion createPromotion(Long sellerId,
                                     PromotionRequestDto promotionRequestDto) {

        Promotion saved = promotionRepository.save(promotionRequestDto.toEntity(sellerId));

        return saved;
    }

    public Page<Promotion.Vo> getPromotionList(Pageable pageable) {
        Page<Promotion> promotionPage = promotionRepository.findAll(pageable);

        if(promotionPage.getTotalElements() <= 0) {
            log.warn("No PROMOTION found for the given page request: {}", pageable);
            return Page.empty();
        }

        return promotionPage.map(promotion -> {
            try {
                return promotion.toVo();
            } catch (Exception e) {
                log.error("Error converting Promotion to Vo: {}", promotion);
                throw new RuntimeException(e);
            }
        });

    }

    public Promotion.Vo approvePromotion(Long promotionId, Boolean isApproved) {
        Promotion promotion = promotionRepository.findById(promotionId).orElseThrow(
                () -> new EntityNotFoundException("Promotion not found for promotionId: " + promotionId)
        );

        promotion.setIsApproved(isApproved);

        Promotion saved = promotionRepository.save(promotion);

        return saved.toVo();

    }

    public void deletePromotion(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId).orElseThrow(
                () -> new EntityNotFoundException("Promotion not found for promotionId: " + promotionId)
        );

        promotionRepository.deleteById(promotionId);

    }
}
