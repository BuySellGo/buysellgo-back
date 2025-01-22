package com.buysellgo.chatservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.buysellgo.chatservice.entity.Message;
import com.buysellgo.chatservice.repository.MessageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;                 

@Service        
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final KafkaTemplate<String, Message> kafkaTemplate;

    public List<Message> getUnreadMessages(String chatRoomId, String receiver) {
        // 읽지 않은 메세지를 가져오는 메서드       
        List<Message> messages = messageRepository.findByChatRoomIdAndReadFalse(chatRoomId, Sort.by(Sort.Direction.ASC, "timestamp")); 
        // 읽지 않은 메세지를 읽음으로 변경(수신자가 본인인 경우)
        for (Message message : messages) {
            if (message.getReceiver().equals(receiver)) {
                message.setRead(true);
                messageRepository.save(message);
            }
        }
        return messages;
    }

    public void sendMessage(Map<String,Object> messageData) { 
        // 새로운 메시지 객체 생성
        Message message = new Message(); 
        // 채팅방 ID 설정 (회원쪽 클라이언트에서 회원 아이디로 설정)
        message.setChatRoomId((String) messageData.get("chatRoomId"));
        // 발신자 ID 설정 
        message.setSender((String) messageData.get("sender"));
        // 수신자 ID 설정 
        message.setReceiver((String) messageData.get("receiver"));
        // 메시지 내용 설정
        message.setContents((String) messageData.get("contents"));
        // 현재 시간을 타임스탬프로 설정
        message.setTimestamp(System.currentTimeMillis());
        // 읽음 상태를 false로 초기화
        message.setRead(false);
        // 메시지를 데이터베이스에 저장
        messageRepository.save(message);
        // 읽지 않은 메시지를 Kafka 토픽으로 전송
        kafkaTemplate.send("chat-topic-unread", message);
    }

    public List<String> getChatRoom(String receiver) {
        Set<String> chatRooms = new HashSet<>();    
        List<Message> messages = messageRepository.findByReceiver(receiver);
        for (Message message : messages) {
            chatRooms.add(message.getSender());
        }
        return new ArrayList<>(chatRooms);
    }
}
