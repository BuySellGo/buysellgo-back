package com.buysellgo.qnaservice.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.buysellgo.qnaservice.common.dto.CommonResDto;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;

import jakarta.validation.Valid;
import com.buysellgo.qnaservice.common.auth.JwtTokenProvider;
import com.buysellgo.qnaservice.strategy.common.QnaContext;
import com.buysellgo.qnaservice.service.QnaService;
import com.buysellgo.qnaservice.common.auth.TokenUserInfo;
import com.buysellgo.qnaservice.strategy.common.QnaStrategy;
import com.buysellgo.qnaservice.strategy.common.QnaResult;
import com.buysellgo.qnaservice.controller.dto.QnaReq;
import com.buysellgo.qnaservice.controller.dto.ReplyReq;
import com.buysellgo.qnaservice.common.exception.CustomException;
import com.buysellgo.qnaservice.service.dto.ServiceResult;
@RequestMapping("/qna")
@RequiredArgsConstructor
@Slf4j
@RestController
public class QnaController {
    private final QnaService qnaService;
    private final QnaContext qnaContext;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary ="Qna 작성(회원)")
    @PostMapping("/question")
    public ResponseEntity<CommonResDto<Map<String, Object>>> createQna(@RequestHeader("Authorization") String accessToken,
    @Valid @RequestBody QnaReq req){
        TokenUserInfo userInfo = jwtTokenProvider.getTokenUserInfo(accessToken);
        QnaStrategy<Map<String, Object>> strategy = qnaContext.getStrategy(userInfo.getRole());
        QnaResult<Map<String, Object>> result = strategy.createQna(req, userInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, result.message(), result.data()));
    }

    @Operation(summary ="Qna 수정")
    @PutMapping("/question/update")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateQna(@RequestHeader("Authorization") String accessToken,
    @Valid @RequestBody QnaReq req, @RequestParam long qnaId){
        TokenUserInfo userInfo = jwtTokenProvider.getTokenUserInfo(accessToken);
        QnaStrategy<Map<String, Object>> strategy = qnaContext.getStrategy(userInfo.getRole());
        QnaResult<Map<String, Object>> result = strategy.updateQna(req, userInfo.getId(), qnaId);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, result.message(), result.data()));
    }

    @Operation(summary ="Qna 조회(회원,판매자,관리자)")
    @GetMapping("/list")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getQna(@RequestHeader("Authorization") String accessToken){
        TokenUserInfo userInfo = jwtTokenProvider.getTokenUserInfo(accessToken);
        QnaStrategy<Map<String, Object>> strategy = qnaContext.getStrategy(userInfo.getRole());
        QnaResult<Map<String, Object>> result = strategy.getQna(userInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, result.message(), result.data()));
    }

    @Operation(summary ="Qna 조회(비회원)")
    @GetMapping("/list/guest")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getQnaGuest(@RequestParam Long productId){
        ServiceResult<Map<String, Object>> result = qnaService.getQnaGuest(productId);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, result.message(), result.data()));  
    }

    @Operation(summary ="Qna 삭제(회원,관리자)")
    @DeleteMapping("/delete")   
    public ResponseEntity<CommonResDto<Map<String, Object>>> deleteQna(@RequestHeader("Authorization") String accessToken,
    @RequestParam Long qnaId){
        TokenUserInfo userInfo = jwtTokenProvider.getTokenUserInfo(accessToken);
        QnaStrategy<Map<String, Object>> strategy = qnaContext.getStrategy(userInfo.getRole());
        QnaResult<Map<String, Object>> result = strategy.deleteQna(qnaId, userInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, result.message(), result.data()));
    }

    @Operation(summary ="Qna 답변 작성(판매자)")
    @PostMapping("/answer")
    public ResponseEntity<CommonResDto<Map<String, Object>>> createAnswer(@RequestHeader("Authorization") String accessToken,
    @Valid @RequestBody ReplyReq req){
        TokenUserInfo userInfo = jwtTokenProvider.getTokenUserInfo(accessToken);
        QnaStrategy<Map<String, Object>> strategy = qnaContext.getStrategy(userInfo.getRole());
        QnaResult<Map<String, Object>> result = strategy.createReply(req, userInfo.getId());
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, result.message(), result.data()));
    }

    @Operation(summary ="Qna 답변 수정(판매자)")
    @PutMapping("/answer/update")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateAnswer(@RequestHeader("Authorization") String accessToken,
    @Valid @RequestBody ReplyReq req, @RequestParam long replyId){
        TokenUserInfo userInfo = jwtTokenProvider.getTokenUserInfo(accessToken);
        QnaStrategy<Map<String, Object>> strategy = qnaContext.getStrategy(userInfo.getRole());
        QnaResult<Map<String, Object>> result = strategy.updateReply(req, userInfo.getId(), replyId);
        if(!result.success()){
            throw new CustomException(result.message());
        }
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, result.message(), result.data()));
    }
}
