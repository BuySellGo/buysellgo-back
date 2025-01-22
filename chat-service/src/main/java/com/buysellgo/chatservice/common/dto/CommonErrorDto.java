package com.buysellgo.chatservice.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
@ToString
@NoArgsConstructor
public class CommonErrorDto {

    private HttpStatus status;
    private String message;
    private Map<String, String> errors;

    public CommonErrorDto(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.errors = new HashMap<>();
    }

    public CommonErrorDto(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors != null ? errors : new HashMap<>();
    }
}
