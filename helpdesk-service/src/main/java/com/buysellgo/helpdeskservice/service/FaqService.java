package com.buysellgo.helpdeskservice.service;

import com.buysellgo.helpdeskservice.dto.FaqRequestDto;
import com.buysellgo.helpdeskservice.entity.Faq;
import com.buysellgo.helpdeskservice.entity.FaqGroup;
import com.buysellgo.helpdeskservice.repository.FaqGroupRepository;
import com.buysellgo.helpdeskservice.repository.FaqRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FaqService {
    private final FaqRepository faqRepository;
    private final FaqGroupRepository faqGroupRepository;

    public Faq createFaq(FaqRequestDto faqRequestDto) {

        FaqGroup faqGroup = faqGroupRepository.findById(faqRequestDto.getFaqGroup().getId()).orElseThrow(
                () -> new EntityNotFoundException("Faq group not found")
        );

        Faq saved = faqRepository.save(faqRequestDto.toEntity());
        log.info("Created faq: {}", saved);

        return saved;
    }
}
