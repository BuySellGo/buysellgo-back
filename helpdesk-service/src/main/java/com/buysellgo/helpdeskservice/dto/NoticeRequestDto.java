package com.buysellgo.helpdeskservice.dto;

import com.buysellgo.helpdeskservice.entity.Notice;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeRequestDto {
    private Long userId;
    private String title;
    private String content;

    public Notice toEntity(){
        return Notice.of(userId, title, content);
    }
}
