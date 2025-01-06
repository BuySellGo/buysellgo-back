package com.buysellgo.helpdeskservice.repository;

import com.buysellgo.helpdeskservice.entity.OneToOneInquiry;
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
class OneToOneInquiryRepositoryTest {

    @Autowired
    private OneToOneInquiryRepository onetoOneInquiryRepository;

    @Test
    @DisplayName("1:1 문의 테이블/데이터 생성 테스트")
    void createOneToOneInquiryTest() {
        // given
        Long userId = 1L;
        String content = """
                일요일도 배송하나요 ?
                """;

        // when
        OneToOneInquiry oneToOneInquiry = onetoOneInquiryRepository.save(OneToOneInquiry.of(userId, content));

        // then
        assertNotNull(oneToOneInquiry);
    }
}