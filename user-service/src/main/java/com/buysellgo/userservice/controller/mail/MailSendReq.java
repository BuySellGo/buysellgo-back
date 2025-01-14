package com.buysellgo.userservice.controller.mail;

import io.swagger.v3.oas.annotations.media.Schema;

public record MailSendReq(
        @Schema(title = "이메일", example = "test@example.com", description = "이메일 주소")
        String email,

        @Schema(title = "전송 유형", example = "VERIFY", description = "전송할 메일의 유형 (VERIFY 또는 PASSWORD)")
        SendType type
) {}
