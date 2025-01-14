package com.buysellgo.userservice.controller.mail;

public enum SendType {
    VERIFY, PASSWORD;

    public String getValue() {
        return this.toString();
    }
}

