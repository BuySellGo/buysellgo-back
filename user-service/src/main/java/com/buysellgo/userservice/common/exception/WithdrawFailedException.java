package com.buysellgo.userservice.common.exception;

public class WithdrawFailedException extends RuntimeException {
    public WithdrawFailedException(String message) {
        super(message);
    }
} 