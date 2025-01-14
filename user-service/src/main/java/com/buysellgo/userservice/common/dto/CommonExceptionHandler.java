package com.buysellgo.userservice.common.dto;

import com.buysellgo.userservice.common.exception.CustomException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.web.bind.MissingRequestHeaderException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Arrays;

//@RestControllerAdvice(basePackages = {"com.buysellgo.userservice.controller"})
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    // Controller단에서 발생하는 모든 예외를 일괄 처리하는 클래스


    // 엔터티를 찾지 못했을 때 예외가 발생하고, 이 메서드가 호출될 것이다.
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonErrorDto> entityNotFoundHandler(EntityNotFoundException e) {
        e.printStackTrace();
        CommonErrorDto dto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto);
    }

    // 옳지 않은 입력값 주입시 호출되는 메서드
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonErrorDto> illegalHandler(IllegalArgumentException e){
        log.info("Illegal argument: " + e.getMessage());
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(commonErrorDto);
    }

    // @Valid를 통한 입력값 검증에서 에러 발생시 일괄 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonErrorDto> validHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        CommonErrorDto commonErrorDto = new CommonErrorDto(
            HttpStatus.BAD_REQUEST,
            "Validation failed",
            errors
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(commonErrorDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonErrorDto> validHandler(AccessDeniedException e){
        e.printStackTrace();
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.UNAUTHORIZED,"arguments not valid");
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(commonErrorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorDto> exceptionHandler(Exception e){
        e.printStackTrace();
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR,"server error");
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(commonErrorDto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CommonErrorDto> handleNoSuchElementException(NoSuchElementException e) {
        log.error("No Such Element: {}", e.getMessage());
        
        // 메시지에 따라 다른 상태 코드 반환
        if (e.getMessage().equals("Refresh token not found")) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonErrorDto(HttpStatus.UNAUTHORIZED, e.getMessage()));
        }
        
        // 그 외의 경우는 기존대로 404 반환
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<CommonErrorDto> missingRequestHeaderHandler(MissingRequestHeaderException e) {
        log.error("Missing Request Header: {}", e.getMessage());
        CommonErrorDto dto = new CommonErrorDto(
            HttpStatus.BAD_REQUEST, 
            "Required request header 'Authorization' for method parameter type String is not present"
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<CommonErrorDto> jwtExceptionHandler(JwtException e) {
        log.error("JWT Exception: {}", e.getMessage());
        CommonErrorDto dto = new CommonErrorDto(
            HttpStatus.UNAUTHORIZED, 
            e.getMessage()
        );
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonErrorDto> withdrawFailedHandler(CustomException e) {
        log.error("Custom Auth Exception: {}", e.getMessage());
        CommonErrorDto dto = new CommonErrorDto(
            HttpStatus.BAD_REQUEST, 
            e.getMessage()
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonErrorDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        
        if (ex.getCause() instanceof InvalidFormatException ife) {
            String fieldName = ife.getPath().get(0).getFieldName();
            String message = String.format("허용되지 않는 값입니다. (허용값: %s)", 
                Arrays.toString(ife.getTargetType().getEnumConstants())
                    .replace("[", "")
                    .replace("]", ""));
            errors.put(fieldName, message);
        }

        CommonErrorDto dto = new CommonErrorDto(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", errors);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto);
    }

}

















