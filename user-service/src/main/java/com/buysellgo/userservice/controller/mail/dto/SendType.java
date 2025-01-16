package com.buysellgo.userservice.controller.mail.dto;

public enum SendType {
    VERIFY, PASSWORD;

    public String getValue() {
        return this.toString();
    }
}

