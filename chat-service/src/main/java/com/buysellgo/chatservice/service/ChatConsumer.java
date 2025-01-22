package com.buysellgo.chatservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.buysellgo.chatservice.entity.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatConsumer { 
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messageTemplate;

    @KafkaListener(topics = "chat-topic-unread", groupId = "chat-group")
    public void consumeUnRead(String messageData) {
        // Kafka 토픽에서 읽지 않은 메시지를 소비하는 메서드
        // 읽지 않은 것을 처리하는 메서드는 메세지 조회쪽에서 구현함
        processMessage(messageData);
    }

    private void processMessage(String messageData) {
        // 메시지 객체를 저장할 변수 선언
        Message message = null;
        try {
            // JSON 문자열을 Message 객체로 변환
            message = objectMapper.readValue(messageData, Message.class);
        } catch (JsonProcessingException e) {
            // JSON 파싱 에러 발생 시 로그 출력
            log.info(e.getMessage());
        }
        // WebSocket을 통해 클라이언트에게 메시지 전송
        // 채팅방 ID를 기반으로 특정 토픽으로 메시지 전송
        messageTemplate.convertAndSend("/topic/chat/" + Objects.requireNonNull(message).getChatRoomId(), message);
    }
}

