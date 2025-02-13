package com.buysellgo.helpdeskservice.repository;

import com.buysellgo.helpdeskservice.entity.Faq;
import com.buysellgo.helpdeskservice.entity.FaqGroup;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
class FaqRepositoryTest {

    @Autowired
    FaqRepository faqRepository;

    @Autowired
    FaqGroupRepository faqGroupRepository;

    @Test
    @DisplayName("FAQ 테이블/데이터 생성 테스트")
    void createFaqTest() {
        // given
        FaqGroup faqGroup = faqGroupRepository.save(FaqGroup.of("배송"));
        String faqTitle = "배송지 주소를 변경하고 싶어요.";
        String faqContent = """
                아래의 순서대로 기본 배송지를 변경할 수 있습니다.
                기본 배송지 변경하기
                ① [마이페이지]에서 [배송지관리] 클릭
                ② [수정] 클릭 후 주소 변경
                ③ 하단에 [기본 배송지로 선택] 체크
                ④ [저장] 클릭
                """;
        // when
        Faq faq = faqRepository.save(Faq.of(faqGroup, faqTitle, faqContent));

        // then
        assertNotNull(faq);
    }
}