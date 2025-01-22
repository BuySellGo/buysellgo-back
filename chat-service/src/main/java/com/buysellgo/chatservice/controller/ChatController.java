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

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/chat/unread")
    public ResponseEntity<List<Message>> getUnreadMessages(@RequestParam String chatRoomId, @RequestParam String receiver) {
        log.info("/chat/unread");
        // 읽지 않은 메세지를 가져오는 메서드
        List<Message> messages = chatService.getUnreadMessages(chatRoomId, receiver);
        return ResponseEntity.ok().body(messages);
    }

    @MessageMapping("/chat/send")
    public void sendMessage( Map<String,Object> message) {
        log.info("/chat/send");
        // 메세지를 보내는 메서드
        chatService.sendMessage(message);
    }

    @GetMapping("/chat/room")
    public ResponseEntity<List<String>> getChatRoom(@RequestParam String receiver) {
        log.info("/chat/room");
        // 채팅방을 가져오는 메서드
        // 채팅방 아이디를 반환
        List<String> chatRooms = chatService.getChatRoom(receiver);
        return ResponseEntity.ok().body(chatRooms);
    }
}