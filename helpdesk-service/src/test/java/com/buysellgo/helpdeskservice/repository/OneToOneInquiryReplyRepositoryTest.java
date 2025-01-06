package com.buysellgo.helpdeskservice.repository;

import com.buysellgo.helpdeskservice.entity.OneToOneInquiry;
import com.buysellgo.helpdeskservice.entity.OneToOneInquiryReply;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = true)
@Transactional
class OneToOneInquiryReplyRepositoryTest
{

    @Autowired
    private OneToOneInquiryReplyRepository oneToOneInquiryReplyRepository;

    @Autowired
    private OneToOneInquiryRepository oneToOneInquiryRepository;

    @Test
    @DisplayName("1:1 문의 답변 테이블/데이터 생성 테스트")
    void createOneOneToInquiryReplyTest() {
        // given
        OneToOneInquiry oneToOneInquiry = oneToOneInquiryRepository.save(OneToOneInquiry.of(1L, "일요일 배송하나요 ?"));
        Long userId = 1L;
        String content = "네, 일요일 배송합니다.";

        // when
        OneToOneInquiryReply oneToOneInquiryReply =
                oneToOneInquiryReplyRepository.save(OneToOneInquiryReply.of(oneToOneInquiry, userId, content));

        // then
        assertNotNull(oneToOneInquiryReply);
    }

}