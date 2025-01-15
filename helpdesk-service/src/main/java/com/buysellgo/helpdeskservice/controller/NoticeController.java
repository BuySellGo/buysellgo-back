package com.buysellgo.helpdeskservice.controller;

import com.buysellgo.helpdeskservice.common.auth.JwtTokenProvider;
import com.buysellgo.helpdeskservice.common.auth.TokenUserInfo;
import com.buysellgo.helpdeskservice.common.dto.CommonResDto;
import com.buysellgo.helpdeskservice.dto.NoticeRequestDto;
import com.buysellgo.helpdeskservice.entity.Notice;
import com.buysellgo.helpdeskservice.repository.NoticeRepository;
import com.buysellgo.helpdeskservice.service.NoticeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.ToOne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
@Slf4j
public class NoticeController {
    private final NoticeService noticeService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 등록(관리자)")
    @PostMapping("/write")
    public ResponseEntity<?> noticeCreate(@AuthenticationPrincipal TokenUserInfo tokenUserInfo,
                                          @RequestBody NoticeRequestDto noticeRequestDto) {

        log.info("noticeRequestDto: {}", noticeRequestDto);

        Notice notice = noticeService.createNotice(noticeRequestDto, tokenUserInfo.getId());

        CommonResDto resDto = new CommonResDto(
                HttpStatus.CREATED, "공지사항 작성 성공", notice.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    @Operation(summary = "공지사항 리스트 조회(공통)")
    @GetMapping("/list")
    public ResponseEntity<?> noticeList(@AuthenticationPrincipal TokenUserInfo tokenUserInfo, Pageable pageable) {
//  URL: {localhost}/helpdesk-service/api/v1/notice/list?page=1&size=3&sort=id.ASC
        log.info("pageable: {}", pageable);

        Page<Notice.Vo> noticeList = noticeService.getNoticeList(pageable);

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "공지사항 리스트 조회 성공", noticeList);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @Operation(summary = "공지사항 리스트 조회(공통)")
    @GetMapping("/list-wo-page")
    public ResponseEntity<?> noticeListWoPage(@AuthenticationPrincipal TokenUserInfo tokenUserInfo) {
        List<Notice.Vo> allNotices = noticeService.getAllNotices();

        CommonResDto resDto = new CommonResDto(
                HttpStatus.OK, "공지사항 리스트 조회 성공", allNotices
        );

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @Operation(summary = "공지사항 조회(공통)")
    @GetMapping("/{id}")
    public ResponseEntity<?> noticeDetail(@AuthenticationPrincipal TokenUserInfo tokenUserInfo, @PathVariable Long id) {

        Notice.Vo notice = noticeService.getNotice(id);

        CommonResDto resDto = new CommonResDto(
                HttpStatus.OK, "공지사항 조회 성공", notice
        );

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 수정(관리자)")
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> noticeEdit(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestBody NoticeRequestDto noticeRequestDto,
            @PathVariable Long id) {

        Notice notice = noticeService.editNotice(id, noticeRequestDto, tokenUserInfo.getId());

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "공지사항 수정 성공", notice.getId());

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "공지사항 삭제(관리자)")
    @DeleteMapping("/delete")
    public ResponseEntity<?> noticeDelete(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestParam Long id) {

        noticeService.noticeDelete(id);

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "공지사항 삭제 완료", null);

        return new ResponseEntity<>(resDto, HttpStatus.OK);

    }

}
