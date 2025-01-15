package com.buysellgo.helpdeskservice.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeReponseDto {
    private Long id;
    private Long userId;
    private String title;
    private Timestamp createdAt;
    private Timestamp startDate;
    private Timestamp endDate;
}
