package com.buysellgo.userservice.controller.info.dto;

import com.buysellgo.userservice.common.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SellerUpdateReq extends InfoUpdateReq{
    @NotNull(message = "판매자 정보인지 비밀번호인지 구분은 필수 입니다.")
    @Schema(title = "판매자 정보인지 비밀번호인지 구분", example = "INFO", description = "판매자 정보인지 비밀번호인지 구분 (INFO: 정보, PASSWORD: 비밀번호)", requiredMode = Schema.RequiredMode.REQUIRED)
    private final UpdateType type;

    @Schema(title = "회사명 변경", example = "부매고 주식회사", description = "회사명 변경")
    private final String companyName;

    @Schema(title = "대표자명 변경", example = "홍길동", description = "대표자명 변경")
    private final String presidentName;

    @Schema(title = "주소 변경", example = "서울시 강남구 역삼동 123-456", description = "주소 변경")
    private final Address address;

    @Schema(title = "비밀번호 변경", example = "test1234!", description = "비밀번호 변경")
    @Pattern(regexp = "^$|^(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=\\S+$).{8,}$", message = "비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다.")
    private final String password;

    public SellerUpdateReq(UpdateType type, String companyName, String presidentName, Address address, String password) {
        this.type = type;
        this.companyName = companyName;
        this.presidentName = presidentName;
        this.address = address;
        this.password = password;
    }
}

