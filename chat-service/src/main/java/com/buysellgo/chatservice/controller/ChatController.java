package com.buysellgo.chatservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;       

import com.buysellgo.chatservice.common.dto.CommonResDto;
import com.buysellgo.chatservice.controller.dto.CreateChatReq;
import com.buysellgo.chatservice.controller.dto.LeaveChatReq;
import com.buysellgo.chatservice.controller.dto.MessageChatReq;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    // 채팅 생성
    @PostMapping("/create")
    public ResponseEntity<CommonResDto<Map<String, Object>>> createChat(@Valid @RequestBody CreateChatReq req) {
        // 필요한 정보: userId, adminId
        // 로직: 1:1 채팅방 생성 및 chatting_id 반환
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "채팅 생성 완료", null));
    }

    // 채팅 메시지 전송
    @PostMapping("/message")
    public ResponseEntity<CommonResDto<Map<String, String>>> sendMessage(@Valid @RequestBody MessageChatReq req) {
        
        // 필요한 정보: chattingId, content, senderId
        // 로직: 메시지 저장 및 전송
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "메시지 전송 완료", null));
    }

    // 채팅 내역 조회
    @GetMapping("/list")
    public ResponseEntity<CommonResDto<Map<String, Object>>> getChatList(@RequestParam String userId,
                                                           @RequestParam int page,
                                                           @RequestParam int size) {
        // 필요한 정보: userId, page, size
        // 로직: 
        // 1. 회원의 경우: 자신이 참여한 채팅방 목록 조회
        // 2. 관리자의 경우: 여러 회원과의 1:1 채팅방 목록 조회
        // 3. 특정 채팅방 선택 시, 해당 채팅방의 메시지 내역 조회 및 페이징 처리
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "채팅 내역 조회 완료", null));
    }

    // 채팅방 나가기
    @PutMapping("/leave")
    public ResponseEntity<CommonResDto<Map<String, String>>> leaveChat(@Valid @RequestBody LeaveChatReq req) {
        // 필요한 정보: chattingId
        // 로직: 사용자가 채팅방을 나가는 처리
        return ResponseEntity.ok().body(new CommonResDto<>(HttpStatus.OK, "채팅방 나가기 완료", null));
    }
}
