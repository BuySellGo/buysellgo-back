package com.buysellgo.helpdeskservice.service;

import com.buysellgo.helpdeskservice.dto.NoticeRequestDto;
import com.buysellgo.helpdeskservice.entity.Notice;
import com.buysellgo.helpdeskservice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public Notice createNotice(NoticeRequestDto noticeRequestDto, long userId) {
        Notice saved = noticeRepository.save(noticeRequestDto.toEntity(userId));
        log.info("saved: {}", saved);
        return saved;
    }
}
