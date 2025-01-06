package com.buysellgo.statisticsservice.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter @Setter
@ToString
@NoArgsConstructor
public class CommonErrorDto {

    private int statusCode;
    private String statusMessage;

    public CommonErrorDto(HttpStatus httpStatus, String statusMessage) {
        this.statusCode = httpStatus.value();
        this.statusMessage = statusMessage;
    }


}
