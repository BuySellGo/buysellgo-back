package com.buysellgo.chatservice.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.buysellgo.chatservice.entity.Message;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    // 읽지 않은 메세지를 가져오는 메서드
    List<Message> findByChatRoomIdAndReadFalse(String chatRoomId, Sort timestamp);
    // 채팅방을 가져오는 메서드
    List<Message> findByReceiver(String receiver);      
    // 채팅방 메세지를 가져오는 메서드
    List<Message> findByChatRoomId(String chatRoomId, Sort timestamp);
}
