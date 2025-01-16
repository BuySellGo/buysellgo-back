package com.buysellgo.helpdeskservice.service;

import com.buysellgo.helpdeskservice.dto.InquiryRequestDto;
import com.buysellgo.helpdeskservice.entity.OneToOneInquiry;
import com.buysellgo.helpdeskservice.repository.OneToOneInquiryReplyRepository;
import com.buysellgo.helpdeskservice.repository.OneToOneInquiryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InquiryService {
    private final OneToOneInquiryRepository oneToOneInquiryRepository;
    private final OneToOneInquiryReplyRepository oneToOneInquiryReplyRepository;
    private JPAQueryFactory queryFactory;


    public OneToOneInquiry questionCreate(
            InquiryRequestDto inquiryRequestDto, Long userId) {

        OneToOneInquiry oneToOneInquiry
                = oneToOneInquiryRepository.save(inquiryRequestDto.toEntity(userId));

        return oneToOneInquiry;
    }
}
