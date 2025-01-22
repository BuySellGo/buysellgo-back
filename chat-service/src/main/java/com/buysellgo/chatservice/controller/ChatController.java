package com.buysellgo.chatservice.controller;

import com.buysellgo.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.messaging.handler.annotation.MessageMapping;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import com.buysellgo.chatservice.entity.Message;
import io.swagger.v3.oas.annotations.Operation;      
import com.buysellgo.chatservice.common.exception.CustomException;              

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;  

    @Operation(summary = "읽지 않은 메시지 조회", description = "채팅방 ID와 수신자 ID를 기반으로 읽지 않은 메시지를 조회합니다.")  
    @GetMapping("/chat/unread")
    public ResponseEntity<List<Message>> getUnreadMessages(@RequestParam String chatRoomId, @RequestParam String receiver) {
        log.info("/chat/unread");
        // 채팅방 ID와 수신자 ID를 기반으로 읽지 않은 메시지를 조회
        // http 메서드로 작성된 것은 기존 게이트웨이에서 http 메서드의 경우 토큰 관련 작업을 해서 필요한 아이디를 추출할 수 있기에 유용하기 때문
        List<Message> messages = chatService.getUnreadMessages(chatRoomId, receiver);
        // 조회된 메시지 목록을 HTTP 200 OK 상태코드와 함께 반환
        // 클라이언트에서는 메세지 목록을 받아서 처리합니다.
        if (messages.isEmpty()) {
            throw new CustomException("읽지 않은 메시지가 없습니다.");
        }
        return ResponseEntity.ok().body(messages);
    }

    @Operation(summary = "메시지 전송", description = "메시지를 전송합니다.")  
    @MessageMapping("/chat/send")
    // @MessageMapping은 WebSocket을 통해 들어오는 메시지를 처리하는 메서드임을 나타냅니다.
    public void sendMessage(Map<String,Object> message) {
        log.info("/chat/send");
        // 메세지를 보내는 메서드
        chatService.sendMessage(message);
    }

    @Operation(summary = "채팅방 조회", description = "채팅방을 조회합니다.")  
    @GetMapping("/chat/room")
    public ResponseEntity<List<String>> getChatRoom(@RequestParam String receiver) {
        log.info("/chat/room");
        // 수신자 ID를 기반으로 채팅방 목록을 조회
        // 현재 서비스에서 이 기능은 관리자만 사용합니다.
        // 관리자와의 1대1 채팅방이기 때문입니다.
        // 채팅방 목록은 수신자와 대화한 모든 발신자의 ID 리스트
        List<String> chatRooms = chatService.getChatRoom(receiver);
        // 조회된 채팅방 목록을 HTTP 200 OK 상태코드와 함께 반환
        // 클라이언트는 이 목록을 사용하여 채팅방 UI를 구성할 수 있음
        if (chatRooms.isEmpty()) {
            throw new CustomException("채팅방이 없습니다.");
        }
        return ResponseEntity.ok().body(chatRooms);
    }

    @Operation(summary = "채팅방 조회", description = "채팅방을 조회합니다.")  
    @GetMapping("/chat/message")
    public ResponseEntity<List<Message>> getChatRoomMessages(@RequestParam String chatRoomId) {
        log.info("/chat/message");
        // 채팅방 ID를 기반으로 메시지 목록을 조회
        List<Message> messages = chatService.getChatRoomMessages(chatRoomId);
        if (messages.isEmpty()) {
            throw new CustomException("채팅방 메시지가 없습니다.");
        }   
        return ResponseEntity.ok().body(messages);
    }
}