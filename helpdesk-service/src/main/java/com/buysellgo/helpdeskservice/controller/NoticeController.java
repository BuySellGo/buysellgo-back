package com.buysellgo.helpdeskservice.controller;

import com.buysellgo.helpdeskservice.common.auth.JwtTokenProvider;
import com.buysellgo.helpdeskservice.common.dto.CommonResDto;
import com.buysellgo.helpdeskservice.dto.NoticeRequestDto;
import com.buysellgo.helpdeskservice.entity.Notice;
import com.buysellgo.helpdeskservice.repository.NoticeRepository;
import com.buysellgo.helpdeskservice.service.NoticeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class NoticeController {
    private final NoticeRepository noticeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NoticeService noticeService;

    @PostMapping("/write")
    public ResponseEntity<?> noticeWrite(@Valid @RequestBody NoticeRequestDto noticeRequestDto) {

        log.info("noticeRequestDto: {}", noticeRequestDto);

        noticeService.noti

//        ObjectMapper objectMapper = new ObjectMapper();

        CommonResDto resDto = new CommonResDto(
                HttpStatus.CREATED, "공지사항 작성 성공", );

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }
}
