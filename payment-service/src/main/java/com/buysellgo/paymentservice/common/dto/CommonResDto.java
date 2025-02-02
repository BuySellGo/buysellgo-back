package com.buysellgo.paymentservice.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter @Setter @ToString
@NoArgsConstructor
public class CommonResDto<T> {

    private int statusCode;
    private String statusMessage;
    private T result;

    public CommonResDto(HttpStatus httpStatus, String statusMessage, T result) {
        this.statusCode = httpStatus.value();
        this.statusMessage = statusMessage;
        this.result = result;
    }
}
