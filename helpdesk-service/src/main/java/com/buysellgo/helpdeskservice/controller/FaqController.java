package com.buysellgo.helpdeskservice.controller;

import com.buysellgo.helpdeskservice.common.auth.TokenUserInfo;
import com.buysellgo.helpdeskservice.common.dto.CommonResDto;
import com.buysellgo.helpdeskservice.dto.FaqRequestDto;
import com.buysellgo.helpdeskservice.dto.FaqResponseDto;
import com.buysellgo.helpdeskservice.entity.Faq;
import com.buysellgo.helpdeskservice.service.FaqService;
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
@RequestMapping("/api/v1/faq")
@RequiredArgsConstructor
@Slf4j
public class FaqController {
    private final FaqService faqService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "FAQ 등록(관리자)")
    @PostMapping("/write")
    public ResponseEntity<?> faqCreate(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestBody FaqRequestDto faqRequestDto) {

        Faq faq = faqService.createFaq(faqRequestDto);

        CommonResDto resDto = new CommonResDto(
                HttpStatus.CREATED, "FAQ 작성 성공", faq.getId()
        );

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    @Operation(summary = "FAQ 리스트 조회(공통)")
    @GetMapping("/list")
    public ResponseEntity<?> faqList(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            Pageable pageable) {
//  URL: {localhost}/helpdesk-service/api/v1/faq/list?page=1&size=3&sort=id.DESC

        log.info("pageable: {}", pageable);

        Page<FaqResponseDto> faqList = faqService.getFaqList(pageable);

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "FAQ 리스트 조회 성공", faqList);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "FAQ 수정(관리자)")
    @PutMapping("/edit/{faqId}")
    public ResponseEntity<?> faqEdit(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestBody FaqRequestDto faqRequestDto,
            @PathVariable Long faqId) {

        Faq faq = faqService.editFaq(faqId, faqRequestDto);

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "FAQ 수정 성공", faq.getId());

        return new ResponseEntity<>(resDto, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "FAQ 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<?> faqDelete(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @RequestParam Long faqId) {

        faqService.faqDelete(faqId);

        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "FAQ 삭제 완료", null);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
