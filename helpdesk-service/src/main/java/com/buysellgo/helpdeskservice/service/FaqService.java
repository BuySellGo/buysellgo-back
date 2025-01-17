package com.buysellgo.helpdeskservice.service;

import com.buysellgo.helpdeskservice.dto.FaqResponseDto;
import com.buysellgo.helpdeskservice.dto.FaqRequestDto;
import com.buysellgo.helpdeskservice.entity.Faq;
import com.buysellgo.helpdeskservice.entity.FaqGroup;
import com.buysellgo.helpdeskservice.entity.QFaq;
import com.buysellgo.helpdeskservice.entity.QFaqGroup;
import com.buysellgo.helpdeskservice.repository.FaqGroupRepository;
import com.buysellgo.helpdeskservice.repository.FaqRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FaqService {
    private final FaqRepository faqRepository;
    private final FaqGroupRepository faqGroupRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public Faq createFaq(FaqRequestDto faqRequestDto) {

        FaqGroup faqGroup = faqGroupRepository.findById(faqRequestDto.getFaqGroupId()).orElseThrow(
                () -> new EntityNotFoundException("Faq group not found")
        );

        Faq saved = faqRepository.save(faqRequestDto.toEntity(faqGroupRepository));
        log.info("Created faq: {}", saved);

        return saved;
    }

    public Page<FaqResponseDto> getFaqList(Pageable pageable) {
        QFaq faq = QFaq.faq;
        QFaqGroup faqGroup = QFaqGroup.faqGroup;

        // 페이징 처리: offset 과 limit 을 Pageable 에서 가져옵니다.
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(faq.id,
                        faq.faqTitle,
                        faq.faqContent,
                        faqGroup.id,
                        faqGroup.faqGroupTitle,
                        faq.createdAt)
                .from(faq)
                .leftJoin(faq.faqGroup, faqGroup);

        // Pageable 처리
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        // 데이터 가져오기
        List<Tuple> tuples = query.fetch();
        if(tuples.isEmpty()) {
            log.warn("No faq found");
            return Page.empty();
        }

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(faq.count())
                .from(faq)
                .leftJoin(faq.faqGroup, faqGroup);

        // 전체 카운트를 가져와서 페이징 결과로 반환
        Long totalCount = countQuery.fetchOne();
        totalCount = totalCount == null ? 0L : totalCount;

        // 결과를 DTO 또는 VO로 변환
        List<FaqResponseDto> faqResponseDtoList = tuples.stream()
                .map(tuple -> new FaqResponseDto(
                        tuple.get(faq.id),
                        tuple.get(faq.faqTitle),
                        tuple.get(faq.faqContent),
                        tuple.get(faq.createdAt),
                        tuple.get(faqGroup.id),
                        tuple.get(faqGroup.faqGroupTitle)
                ))
                .collect(Collectors.toList());


        return new PageImpl<>(faqResponseDtoList, pageable, totalCount);
    }

    public Faq editFaq(Long faqId, FaqRequestDto faqRequestDto) {

        FaqGroup faqGroup = faqGroupRepository.findById(faqRequestDto.getFaqGroupId()).orElseThrow(
                () -> new EntityNotFoundException("Faq group not found")
        );

        Faq faq = faqRepository.findById(faqId).orElseThrow(
                () -> new EntityNotFoundException("Faq id: {" + faqId + "} not found")
        );

        faq.update(faqRequestDto, faqGroup);

        Faq savedFaq = faqRepository.save(faq);

        return savedFaq;

    }

    public void faqDelete(Long faqId) {

        Faq faq = faqRepository.findById(faqId).orElseThrow(
                () -> new EntityNotFoundException("FAQ id: {" + faqId + "} not found")
        );

        faqRepository.deleteById(faqId);

    }
}
