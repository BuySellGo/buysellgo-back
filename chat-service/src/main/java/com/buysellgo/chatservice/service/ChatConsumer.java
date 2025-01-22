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
        processMessage(messageData);
    }

    private void processMessage(String messageData) {
        Message message = null;
        try {
            message = objectMapper.readValue(messageData, Message.class);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
        messageTemplate.convertAndSend("/topic/chat/" + Objects.requireNonNull(message).getChatRoomId(), message);
    }
}

