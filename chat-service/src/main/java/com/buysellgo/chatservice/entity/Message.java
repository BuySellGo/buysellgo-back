package com.buysellgo.chatservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "messages")
@Data
public class Message {
    // 메세지 아이디
    @Id
    private String id;
    // 채팅방 아이디
    private String chatRoomId;
    // 메세지 보낸 사람
    private String sender;
    // 메세지 받은 사람
    private String receiver;
    // 메세지 내용
    private String contents;
    // 메세지 보낸 시간
    private long timestamp;
    // 메세지 읽음 여부
    private boolean read;
}
