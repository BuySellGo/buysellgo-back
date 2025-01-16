package com.buysellgo.helpdeskservice.controller;

import com.buysellgo.helpdeskservice.common.auth.TokenUserInfo;
import com.buysellgo.helpdeskservice.common.dto.CommonResDto;
import com.buysellgo.helpdeskservice.dto.InquiryRequestDto;
import com.buysellgo.helpdeskservice.entity.OneToOneInquiry;
import com.buysellgo.helpdeskservice.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/inquiry")
@RequiredArgsConstructor
@Slf4j
public class InquiryController {
    private final InquiryService inquiryService;

    @Operation(summary = "1:1 문의(회원)")
    @PostMapping("/question")
    public ResponseEntity<?> questionCreate(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestBody InquiryRequestDto inquiryRequestDto) {

        // 인증된 사용자 정보 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.UNAUTHORIZED, "Unauthorized", null), HttpStatus.UNAUTHORIZED);
        }

        OneToOneInquiry oneToOneInquiry
                = inquiryService.questionCreate(inquiryRequestDto, tokenUserInfo.getId());

        // 문의 작성 실패 시 처리
        if (oneToOneInquiry == null) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "문의 작성 실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "1:1문의 작성 완료", oneToOneInquiry.getId());
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }

}
