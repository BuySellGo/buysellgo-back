package com.buysellgo.helpdeskservice.repository;

import com.buysellgo.helpdeskservice.entity.FaqGroup;
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
class FaqGroupRepositoryTest {

    @Autowired
    private FaqGroupRepository faqGroupRepository;

    @Test
    @DisplayName("FAQ 그룹 테이블/데이터 생성 테스트")
    void createFaqGroupTest() {
        // given
        String faqTitle = "FAQ 그룹-1";

        // when
        FaqGroup save = faqGroupRepository.save(FaqGroup.of(faqTitle));

        // then
        assertNotNull(save);
    }
}