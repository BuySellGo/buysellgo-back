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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
@Slf4j
public class NoticeController {
    private final NoticeRepository noticeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NoticeService noticeService;

    @Operation(summary = "공지사항 등록(관리자)")
    @PostMapping("/write")
//    public ResponseEntity<?> noticeCreate(@AuthenticationPrincipal TokenUserInfo tokenUserInfo,
    public ResponseEntity<?> noticeCreate(@RequestBody NoticeRequestDto noticeRequestDto) {

        log.info("noticeRequestDto: {}", noticeRequestDto);


        Notice notice = noticeService.createNotice(noticeRequestDto);

        CommonResDto resDto = new CommonResDto(
                HttpStatus.CREATED, "공지사항 작성 성공", notice.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }
}
