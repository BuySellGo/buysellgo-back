package com.buysellgo.userservice.controller.dto;

public record MailSendReq(
        String email,
        SendType type
) {}
