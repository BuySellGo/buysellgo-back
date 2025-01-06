package com.buysellgo.helpdeskservice.common.dto;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    // 옳지 않은 입력값 주입시 호출되는 메서드
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonErrorDto> illegalHandler(IllegalArgumentException e){
        log.info("Illegal argument: " + e.getMessage());
//        e.printStackTrace();
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
    }

    // @Valid를 통한 입력값 검증에서 에러 발생시 일괄 처리
    // @Validated를 사용했다면 ConstraintViolationException 으로 처리 (Spring 제공 아노테이션)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonErrorDto> validHandler(MethodArgumentNotValidException e){
        e.printStackTrace();
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST,"arguments not valid");
        return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonErrorDto> validHandler(AccessDeniedException e){
        e.printStackTrace();
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.UNAUTHORIZED,"arguments not valid");
        return new ResponseEntity<>(commonErrorDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorDto> exceptionHandler(Exception e){
        e.printStackTrace();
        CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR,"server error");
        return new ResponseEntity<>(commonErrorDto, HttpStatus.INTERNAL_SERVER_ERROR); // 500 에러
    }


}

















