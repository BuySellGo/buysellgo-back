package com.buysellgo.helpdeskservice.service;

import com.buysellgo.helpdeskservice.dto.NoticeRequestDto;
import com.buysellgo.helpdeskservice.entity.Notice;
import com.buysellgo.helpdeskservice.repository.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public Page<Notice.Vo> getNoticeList(Pageable pageable) {

        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        if(noticePage.isEmpty()){
           log.warn("No notice found for the given page request: {}", pageable);
           return Page.empty();
        }
//        noticePage.getContent().forEach(notice -> {
//            Notice.Vo vo = notice.toVo();
//            log.info("Notice.Vo: {}", vo);
//        });

        return noticePage.map(notice -> {

            try {
                return notice.toVo();
            } catch (Exception e) {
                log.error("Error converting Notice to Vo:{}", notice, e);
                throw new RuntimeException(e);
            }
        });
//        return noticePage.map(Notice::toVo);
    }

    public List<Notice.Vo> getAllNotices() {
        List<Notice> notices = noticeRepository.findAll();
        return notices.stream().map(Notice::toVo)
                .collect(Collectors.toList());
    }

    public Notice.Vo getNotice(Long id) {

        Notice notice = noticeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("notice not found")
        );

        return notice.toVo();
    }

    public Notice editNotice(Long id, NoticeRequestDto noticeRequestDto, long userId) {

        Notice notice = noticeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("notice id: {" + id + "} not found")
        );

        notice.update(noticeRequestDto, userId);

        Notice saved = noticeRepository.save(notice);

        return saved;

    }

    public void noticeDelete(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("notice id: {" + id + "} not found")
        );

        noticeRepository.deleteById(id);
    }
}
