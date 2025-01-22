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
        // 채팅방 ID와 읽지 않은 메시지를 기준으로 메시지 목록을 조회합니다.
        // timestamp를 기준으로 오름차순 정렬하여 시간순으로 메시지를 가져옵니다.
        List<Message> messages = messageRepository.findByChatRoomIdAndReadFalse(chatRoomId, Sort.by(Sort.Direction.ASC, "timestamp")); 

        // 조회된 메시지 목록을 순회하면서 수신자가 본인인 메시지를 읽음 처리합니다.
        for (Message message : messages) {
            // 메시지의 수신자가 파라미터로 전달받은 receiver와 일치하는 경우
            if (message.getReceiver().equals(receiver)) {
                // 기본적으로 모든 메세지는 읽지 않은 상태로 저장되기 때문에 읽음 상태로 변경합니다.
                // 읽지 않은 상태로 저장하는 이유는 웹소켓 방식에서 쌍방이 접속했다는 이벤트를 인식하는 것을 구현을 못해서 그렇습니다....
                message.setRead(true);
                // 변경된 메시지를 데이터베이스에 저장합니다.
                messageRepository.save(message);
            }
        }
        // 조회된 메시지 목록을 반환합니다.
        // 원래는 카프카를 사용하여 하려고 했는데 
        // 공통된 채팅방을 웹소켓으로 구독하고 있기 때문에 안읽은 메세지를 확인할시 보내는 쪽에서도 중복하여 메세지가 나는 경우가 있었습니다.
        // 그래서 카프카를 사용하지 않고 http 메서드로 돌려주는 방식으로 바꿨습니다.
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
        // 채팅방 목록을 저장할 Set 생성
        // 채팅방 목록은 수신자와 대화한 모든 발신자의 ID 리스트..이기 때문에 중복을 제거하기 위해 Set을 사용합니다.
        Set<String> chatRooms = new HashSet<>();    
        // 수신자 ID를 기반으로 메시지 목록을 조회
        // 조회된 메시지 목록을 순회하면서 발신자 ID를 채팅방 목록에 추가
        List<Message> messages = messageRepository.findByReceiver(receiver);
        for (Message message : messages) {
            chatRooms.add(message.getSender());
        }
        // 채팅방 목록을 반환
        return new ArrayList<>(chatRooms);
    }

    public List<Message> getChatRoomMessages(String chatRoomId) {
        // 채팅방 ID를 기반으로 메시지 목록을 조회
        // 채팅방 메세지는 최신순으로 정렬되어 있기 때문에 Sort.by(Sort.Direction.ASC, "timestamp")를 사용하여 시간순으로 정렬합니다.
        // 조회된 메시지 목록을 반환
        return messageRepository.findByChatRoomId(chatRoomId, Sort.by(Sort.Direction.ASC, "timestamp"));
    }
}
