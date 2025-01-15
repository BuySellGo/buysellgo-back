package com.buysellgo.helpdeskservice.controller;

import com.buysellgo.helpdeskservice.common.auth.TokenUserInfo;
import com.buysellgo.helpdeskservice.common.dto.CommonResDto;
import com.buysellgo.helpdeskservice.dto.FaqRequestDto;
import com.buysellgo.helpdeskservice.entity.Faq;
import com.buysellgo.helpdeskservice.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
