package com.buysellgo.userservice.controller.info;

import com.buysellgo.userservice.common.dto.CommonResDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;        
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
@Slf4j
public class InfoController {
    @Operation(summary = "회원정보 조회")
    @GetMapping("/one")
    public ResponseEntity<CommonResDto> getOne() {
        //회원정보를 하나 조회하는 로직
        //각자의 정보를 조회하는 로직
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "회원정보 조회 성공", null));
    }

    @Operation(summary = "회원정보 리스트 조회(관리자)")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResDto> getList() {
        //회원정보를 여러개 조회하는 로직
        //관리자가 사용하는 로직
        //판매자 전체를 조회하거나, 회원 전체를 조회함
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "회원정보 조회 성공", null));
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping("/edit")
    public ResponseEntity<CommonResDto> edit() {
        //회원정보를 수정하는 로직
        //판매자의 정보를 수정할 수 있음
        //회원의 정보를 수정할 수 있음
        //회원의 경우 개인정보 수정과 프로필 수정이 분리되어 있음
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "회원정보 수정 성공", null));
    }
}
