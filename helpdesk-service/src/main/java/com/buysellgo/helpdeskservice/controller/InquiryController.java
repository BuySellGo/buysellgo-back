package com.buysellgo.helpdeskservice.controller;

import com.buysellgo.helpdeskservice.common.auth.TokenUserInfo;
import com.buysellgo.helpdeskservice.common.dto.CommonResDto;
import com.buysellgo.helpdeskservice.dto.InquiryReplyRequestDto;
import com.buysellgo.helpdeskservice.dto.InquiryRequestDto;
import com.buysellgo.helpdeskservice.dto.RequestIdDto;
import com.buysellgo.helpdeskservice.entity.OneToOneInquiry;
import com.buysellgo.helpdeskservice.entity.OneToOneInquiryReply;
import com.buysellgo.helpdeskservice.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inquiry")
@RequiredArgsConstructor
@Slf4j
public class InquiryController {
    private final InquiryService inquiryService;

    @Operation(summary = "1:1 문의(회원)")
    @PostMapping("/question")
    public ResponseEntity<?> createQuestion(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestBody InquiryRequestDto inquiryRequestDto) {

        // 인증된 사용자 정보 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        OneToOneInquiry oneToOneInquiry
                = inquiryService.createQuestion(inquiryRequestDto, tokenUserInfo.getId());

        // 문의 작성 실패 시 처리
        if (oneToOneInquiry == null) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "문의 작성 실패", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "1:1문의 작성 완료", oneToOneInquiry.getId());
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "1:1문의 답변 리스트 조회(관리자)")
    @GetMapping("/list")
    public ResponseEntity<?> listInquiry(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            Pageable pageable) {
//  URL: {localhost}/helpdesk-service/api/v1/inquiry/list?page=1&size=3&sort=id.ASC

        Page<OneToOneInquiry.Vo> inquireList = inquiryService.getInquireList(pageable);

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "1:1문의 답변 리스트 조회 성공", inquireList);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "1:1문의 답변(관리자)")
    @PostMapping("/reply")
    public ResponseEntity<?> createInquiryReply(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestBody InquiryReplyRequestDto inquiryReplyRequestDto) {
        // 인증된 사용자 정보 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.UNAUTHORIZED, "Unauthorized", null), HttpStatus.UNAUTHORIZED);
        }

        OneToOneInquiryReply oneToOneInquiryReply =
                inquiryService.createInquiryReply(inquiryReplyRequestDto, tokenUserInfo.getId());

        // 문의 작성 실패 시 처리
        if (oneToOneInquiryReply == null) {
            return new ResponseEntity<>(
                    new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR,
                            "문의 답변 작성 실패",
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto resDto = new CommonResDto(
                HttpStatus.CREATED,
                "1:1문의 답변 작성 성공",
                oneToOneInquiryReply.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }

    @Operation(summary = "1:1문의 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInquiry(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestBody RequestIdDto requestIdDto) {

        inquiryService.deleteInquiry(requestIdDto.getRequestId());

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "1:1문의 삭제 성공", null);

        return new ResponseEntity<>(resDto, HttpStatus.OK);

    }
}
