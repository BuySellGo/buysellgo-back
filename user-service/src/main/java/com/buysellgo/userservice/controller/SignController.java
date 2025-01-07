package com.buysellgo.userservice.controller;

import com.buysellgo.userservice.common.dto.CommonResDto;
import com.buysellgo.userservice.controller.dto.UserCreateReq;
import com.buysellgo.userservice.controller.dto.SellerCreateReq;
import com.buysellgo.userservice.domain.seller.Seller;
import com.buysellgo.userservice.domain.user.User;
import com.buysellgo.userservice.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.buysellgo.userservice.common.util.CommonConstant.*;

@Slf4j
@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @Operation(summary = "회원가입 요청(회원)")
    @PostMapping("/user")
    public ResponseEntity<CommonResDto> userSign(@Valid @RequestBody UserCreateReq req) {
        User.Vo vo = signService.userSign(req);
        CommonResDto dto = new CommonResDto(HttpStatus.CREATED, "회원가입 성공(회원)", vo);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "회원가입 요청(판매자)")
    @PostMapping("/seller")
    public ResponseEntity<CommonResDto> sellerSign(@Valid @RequestBody SellerCreateReq req) {
        Seller.Vo vo = signService.sellerSign(req);
        CommonResDto dto = new CommonResDto(HttpStatus.CREATED,"회원가입 성공(판매자)",vo);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "회원탈퇴 요청(회원)")
    @DeleteMapping("/user")
    public ResponseEntity<CommonResDto> userDelete(
            @RequestHeader(value = "Authorization", required = true) String accessToken
    ){
        String token = accessToken.replace(BEARER_PREFIX.getValue(), "");
        signService.userDelete(token);
        CommonResDto dto =  new CommonResDto(HttpStatus.OK,"회원 탈퇴 완료(회원)",null);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
