package com.buysellgo.helpdeskservice.repository;

import com.buysellgo.helpdeskservice.entity.Notice;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = true)
@Transactional
class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("공지사항 테이블/데이터 생성 테스트")
    void createNoticeTest() {
        // given
        Long userId = 1L;
        String title = "공지사항";
        String content = "오늘 돼지 잡는 날입니다.";
        Timestamp startDate = Timestamp.from(Instant.now());
        Timestamp endDate = Timestamp.from(Instant.now().plus(Duration.ofDays(3)));

        // when
        Notice notice = Notice.of(userId, title, startDate, endDate, content);
        Notice save = noticeRepository.save(notice);

        // then
        assertNotNull(save.getId(),"공지사항 ID 생성됨.");
    }
}