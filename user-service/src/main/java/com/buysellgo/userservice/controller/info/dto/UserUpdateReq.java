package com.buysellgo.userservice.controller.info.dto;

import java.time.LocalDate;     
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;  
import jakarta.validation.constraints.Size;

import com.buysellgo.userservice.domain.user.Gender;

@Getter
public class UserUpdateReq extends InfoUpdateReq{
    
    @Schema(title = "업데이트 유형", example = "INFO", description = "업데이트 유형 (INFO: 정보, PROFILE: 프로필, PASSWORD: 비밀번호)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "업데이트 유형은 필수 입니다.")
    private final UpdateType type;

    @Size(min = 3, max = 50)
    @Schema(description = "닉네임 변경", example = "홍길동")
    private final String username;

    @Schema(description = "휴대폰 번호 변경", example = "010-1234-5678")
    private final String phone;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/path/to/image.jpg")
    private final String profileImageUrl;

    @Schema(description = "나이 변경", example = "20")
    private final String ages;

    @Schema(description = "성별 변경", example = "MALE")
    private final Gender gender;

    @Schema(description = "생일 변경", example = "1990-01-01")
    private final LocalDate birthday;

    @Schema(description = "비밀번호 변경", example = "test1234!")
    @Pattern(regexp = "^$|^(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=\\S+$).{8,}$", message = "비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다.")
    private final String password;

    public UserUpdateReq(UpdateType type, String username, String phone, String profileImageUrl, String ages, Gender gender, LocalDate birthday, String password) {
        this.type = type;
        this.username = username;
        this.phone = phone;
        this.profileImageUrl = profileImageUrl;
        this.ages = ages;
        this.gender = gender;
        this.birthday = birthday;
        this.password = password;
    }   
}
