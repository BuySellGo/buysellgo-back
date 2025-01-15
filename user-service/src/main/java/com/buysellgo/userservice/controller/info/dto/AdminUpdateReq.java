package com.buysellgo.userservice.controller.info.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;


@Getter
public class AdminUpdateReq extends InfoUpdateReq{
    @NotNull(message = "관리자 정보인지 비밀번호인지 구분은 필수 입니다.")
    @Schema(title = "관리자 정보인지 비밀번호인지 구분", example = "INFO", description = "관리자 정보인지 비밀번호인지 구분 (INFO: 정보, PASSWORD: 비밀번호)", requiredMode = Schema.RequiredMode.REQUIRED)
    private final UpdateType type;

    @Schema(title = "비밀번호 변경", example = "test1234!", description = "비밀번호 변경")
    @Pattern(regexp = "^$|^(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=\\S+$).{8,}$", message = "비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다.")
    private final String password;

    public AdminUpdateReq(UpdateType type, String password) {
        this.type = type;
        this.password = password;
    }
}
