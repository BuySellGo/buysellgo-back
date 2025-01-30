package com.buysellgo.qnaservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import com.buysellgo.qnaservice.common.dto.CommonResDto;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;   
import org.springframework.web.bind.annotation.GetMapping;

@RequestMapping("/qna")
@RequiredArgsConstructor
@Slf4j
@RestController
public class QnaController {
    

    @Operation(summary ="Qna 작성(회원)")
    @PostMapping("/question")
    public ResponseEntity<CommonResDto<Map<String, Object>>> createQna(){
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "Qna 작성 완료", null));  
    }

    @Operation(summary ="Qna 수정")
    @PutMapping("/update")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateQna(){
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "Qna 수정 완료", null));  
    }

    @Operation(summary ="Qna 조회(회원,판매자,관리자)")
    @GetMapping("/list")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getQna(){
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "Qna 조회 완료", null));  
    }

    @Operation(summary ="Qna 조회(비회원)")
    @GetMapping("/list/guest")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getQnaGuest(){
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "Qna 조회 완료", null));  
    }

    @Operation(summary ="Qna 삭제(회원,관리자)")
    @DeleteMapping("/delete")   
    public ResponseEntity<CommonResDto<Map<String, Object>>> deleteQna(){
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "Qna 삭제 완료", null));  
    }

    @Operation(summary ="Qna 답변 작성(판매자)")
    @PostMapping("/answer")
    public ResponseEntity<CommonResDto<Map<String, Object>>> createAnswer(){
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "Qna 답변 작성 완료", null));  
    }

    @Operation(summary ="Qna 답변 수정(판매자)")
    @PutMapping("/answer/update")
    public ResponseEntity<CommonResDto<Map<String, Object>>> updateAnswer(){
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "Qna 답변 수정 완료", null));  
    }
}
