package com.buysellgo.orderservice.controller.dto;
import java.util.List;
import com.buysellgo.orderservice.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;



public record CartOrderReq(
    @Schema(description = "장바구니 아이디 리스트", example = "[1, 2, 3]")
    @NotNull(message = "장바구니 아이디 리스트는 필수 입력 사항입니다.")
    @Size(min = 1, message = "장바구니 아이디 리스트는 최소 1개 이상 포함되어야 합니다.")
    List<Long> cartIds,
    @Schema(description = "메모", example = "배송 주소")
    @NotNull(message = "메모는 필수 입력 사항입니다.")
    String memo,
    @Schema(description = "결제 방법", example = "KAKAO_PAY")
    @NotNull(message = "결제 방법은 필수 입력 사항입니다.")
    PaymentMethod paymentMethod

) {


}
