package com.buysellgo.userservice.controller.sign.dto;

import com.buysellgo.userservice.common.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.Valid;

public record SellerCreateReq(
        @Schema(title = "회사명", example = "부매고 주식회사", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "회사명은 필수 입니다.")
        String companyName,

        @Schema(title = "대표자명", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "대표자명은 필수 입니다.")
        String presidentName,

        @Schema(title = "주소", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "주소는 필수 입니다.")
        @Valid
        Address address,

        @Schema(title = "이메일", example = "seller@buysellgo.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "이메일은 필수 입니다.")
        @Pattern(
                regexp = "^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$",
                message = "올바른 이메일 형식이 아닙니다."
        )
        String email,

        @Schema(title = "비밀번호", example = "test1234!", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "비밀번호는 필수 입니다.")
        @Pattern(
                regexp = "^$|(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
                message = "비밀번호는 최소 8자 이상이며, 1개 이상의 숫자와 특수문자를 포함해야 합니다."
        )
        String password,

        @Schema(title = "사업자등록번호", example = "123-45-67890", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "사업자등록번호는 필수 입니다.")
        @Pattern(
                regexp = "^$|^\\d{3}-\\d{2}-\\d{5}$",
                message = "올바른 사업자등록번호 형식이 아닙니다."
        )
        String businessRegistrationNumber,

        @Schema(title = "사업자등록증 이미지", example = "business_registration.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "사업자등록증 이미지는 필수 입니다.")
        String businessRegistrationNumberImg
) {}
