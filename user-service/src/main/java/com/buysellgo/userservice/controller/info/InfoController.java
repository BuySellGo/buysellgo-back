package com.buysellgo.userservice.controller.info;

import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.common.entity.Role;
import com.buysellgo.userservice.common.exception.CustomException;
import com.buysellgo.userservice.strategy.info.common.InfoContext;
import com.buysellgo.userservice.strategy.info.common.InfoResult;
import com.buysellgo.userservice.strategy.info.common.InfoStrategy;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.buysellgo.userservice.controller.info.dto.InfoUpdateReq;
import com.buysellgo.userservice.controller.info.dto.InfoListReq;
import java.util.Map;


@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
@Slf4j
public class InfoController {
    private final InfoContext infoContext;

    @Operation(summary = "회원정보 조회")
    @GetMapping("/one")
    public ResponseEntity<CommonResDto<Object>> getOne(@RequestHeader("X-User-Role") Role role,
                                             @RequestHeader("X-User-Email") String email) {
        InfoStrategy<Map<String, Object>> strategy = infoContext.getStrategy(role);
        InfoResult<Map<String, Object>> result = strategy.getOne(email);
        if(!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "회원정보 조회 성공", result));
    }

    @Operation(summary = "회원정보 리스트 조회(관리자)")
    @PostMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResDto<Object>> getList(@Valid @RequestBody InfoListReq infoListReq,
                                                        @RequestHeader("X-User-Role") Role role) {
        InfoStrategy<Map<String, Object>> strategy = infoContext.getStrategy(role);
        InfoResult<Map<String, Object>> result = strategy.getList(infoListReq.role());
        if(!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "회원정보 조회 성공", result));
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping("/edit")
    public ResponseEntity<CommonResDto<Object>> edit(@Valid @RequestBody InfoUpdateReq infoUpdateReq,
                                             @RequestHeader("X-User-Role") Role role,
                                             @RequestHeader("X-User-Email") String email) {
        InfoStrategy<Map<String, Object>> strategy = infoContext.getStrategy(role);
        InfoResult<Map<String, Object>> result = strategy.edit(infoUpdateReq, email);
        if(!result.success()) {
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "회원정보 수정 성공", result));
    }
}
