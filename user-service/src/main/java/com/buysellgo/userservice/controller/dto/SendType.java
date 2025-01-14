package com.buysellgo.userservice.controller.dto;

public enum SendType {
    VERIFY, PASSWORD;

    public String getValue() {
        return this.toString();
    }
}

