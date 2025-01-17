package com.buysellgo.helpdeskservice.service;

import com.buysellgo.helpdeskservice.dto.InquiryReplyRequestDto;
import com.buysellgo.helpdeskservice.dto.InquiryRequestDto;
import com.buysellgo.helpdeskservice.entity.OneToOneInquiry;
import com.buysellgo.helpdeskservice.entity.OneToOneInquiryReply;
import com.buysellgo.helpdeskservice.repository.OneToOneInquiryReplyRepository;
import com.buysellgo.helpdeskservice.repository.OneToOneInquiryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
public class InquiryService {
    private final OneToOneInquiryRepository oneToOneInquiryRepository;
    private final OneToOneInquiryReplyRepository oneToOneInquiryReplyRepository;
    private JPAQueryFactory queryFactory;


    public OneToOneInquiry createQuestion(
            InquiryRequestDto inquiryRequestDto, Long userId) {

        OneToOneInquiry oneToOneInquiry
                = oneToOneInquiryRepository.save(inquiryRequestDto.toEntity(userId));

        return oneToOneInquiry;
    }

    public Page<OneToOneInquiry.Vo> getInquireList(Pageable pageable) {
        Page<OneToOneInquiry> inquiryPage = oneToOneInquiryRepository.findAll(pageable);

        if (inquiryPage.isEmpty()) {
            log.warn("No inquiry found for the given page request: {}", pageable);
            return Page.empty();
        }

        return inquiryPage.map(oneToOneInquiry -> {
            try {
                return oneToOneInquiry.toVo();
            } catch (Exception e) {
                log.error("Error converting Inquiry to Vo: {}", oneToOneInquiry);
                throw new RuntimeException(e);
            }
        });

//        return inquiryPage.map(OneToOneInquiry::toVo);
    }

    public OneToOneInquiryReply createInquiryReply(
            InquiryReplyRequestDto inquiryReplyRequestDto, Long userId) {
        OneToOneInquiryReply oneToOneInquiryReply =
                oneToOneInquiryReplyRepository.save(
                        inquiryReplyRequestDto.toEntity(
                                userId,
                                oneToOneInquiryRepository));

        return oneToOneInquiryReply;
    }

    public void deleteInquiry(Long inquiryId) {

        OneToOneInquiry oneToOneInquiry = oneToOneInquiryRepository.findById(inquiryId).orElseThrow(
                () -> new EntityNotFoundException("Inquiry with id " + inquiryId + " not found")
        );

        // OneToOneInquiryReply 는  cascade 로 삭제됨.
        oneToOneInquiryRepository.deleteById(inquiryId);

    }
}
