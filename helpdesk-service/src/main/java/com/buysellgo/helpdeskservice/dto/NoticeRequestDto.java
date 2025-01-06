package com.buysellgo.helpdeskservice.dto;

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
    private Timestamp createdAt;
    private Timestamp startDate;
    private Timestamp endDate;
    private String title;
    private String content;
}
