package com.buysellgo.deliveryservice.dto;

import com.buysellgo.deliveryservice.entity.DeliveryAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAddressRequestDto {

    @Schema(description = "사용자 ID", example = "123", required = true)
    @NotNull(message = "사용자 ID는 필수입니다.")
    @Positive(message = "사용자 ID는 양수여야 합니다.")
    private Long userId;

    @Schema(description = "주소 이름", example = "집", required = true)
    @NotEmpty(message = "주소 이름은 필수입니다.")
    @Size(max = 50, message = "주소 이름은 최대 50자까지 입력할 수 있습니다.")
    private String addressName;

    @Schema(description = "수령인", example = "홍길동", required = true)
    @NotEmpty(message = "수령인은 필수입니다.")
    @Size(max = 50, message = "수령인은 최대 50자까지 입력할 수 있습니다.")
    private String receiver;

    @Schema(description = "휴대전화번호", example = "010-1234-5678", required = true)
    @NotEmpty(message = "휴대전화번호는 필수입니다.")
    @Pattern(regexp = "^(\\d{3})-(\\d{3,4})-(\\d{4})$", message = "전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678")
    private String phone;

    @Schema(description = "우편번호", example = "12345")
    @Pattern(regexp = "^[0-9]{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
    private String zipCode;

    @Schema(description = "주소", example = "서울특별시 강남구 역삼동")
    @NotEmpty(message = "주소는 필수입니다.")
    @Size(max = 100, message = "주소는 최대 100자까지 입력할 수 있습니다.")
    private String address;

    @Schema(description = "상세 주소", example = "101동111호")
    @Size(max = 100, message = "상세 주소는 최대 100자까지 입력할 수 있습니다.")
    private String detailAddress;

    @Schema(description = "기본 주소 여부", example = "true")
    @NotNull(message = "기본 주소 여부는 null일 수 없습니다.")
    private Boolean isDefault;

    // 생성일
    @Schema(description = "생성일", example = "2023-01-01T12:00:00")
    private Timestamp createdAt;

    // 수정일
    @Schema(description = "수정일", example = "2023-01-01T12:00:00")
    private Timestamp updatedAt;

    // toEntity 메서드 (DTO -> Entity 변환)
    public DeliveryAddress toEntity() {
        return DeliveryAddress.of(
                userId,
                addressName,
                receiver,
                phone,
                zipCode,
                address,
                detailAddress,
                isDefault,
                createdAt);
    }
}
