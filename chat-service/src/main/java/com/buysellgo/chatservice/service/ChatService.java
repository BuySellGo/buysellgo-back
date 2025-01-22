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
        // 메세지를 보내는 메서드
        Message message = new Message(); 
        // 채팅방 아이디(메세지 보낸 사람과 받은 사람의 아이디를 조합하여 생성)
        message.setChatRoomId((String) messageData.get("chatRoomId"));
        // 메세지 보낸 사람
        message.setSender((String) messageData.get("sender"));
        // 메세지 받은 사람
        message.setReceiver((String) messageData.get("receiver"));
        // 메세지 내용
        message.setContents((String) messageData.get("contents"));
        // 메세지 보낸 시간
        message.setTimestamp(System.currentTimeMillis());
        // 메세지 읽음 여부
        message.setRead(false);
        messageRepository.save(message);
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
