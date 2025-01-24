package com.buysellgo.deliveryservice.controller;

import com.buysellgo.deliveryservice.common.auth.TokenUserInfo;
import com.buysellgo.deliveryservice.common.dto.CommonResDto;
import com.buysellgo.deliveryservice.dto.DeliveryAddressRequestDto;
import com.buysellgo.deliveryservice.dto.DeliveryAddressResponseDto;
import com.buysellgo.deliveryservice.dto.DeliveryStatusRequestDto;
import com.buysellgo.deliveryservice.entity.Delivery;
import com.buysellgo.deliveryservice.entity.DeliveryAddress;
import com.buysellgo.deliveryservice.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "배송지 주소 등록")
    @PostMapping("/add-address")
    public ResponseEntity<?> addDeliveryAddress(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @Valid @RequestBody DeliveryAddressRequestDto deliveryAddressRequestDto) {

        log.info("DeliveryAddressRequestDto: {}", deliveryAddressRequestDto);

        // 인증된 사용자 정보 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        DeliveryAddress deliveryAddress
                = deliveryService.addDeliveryAddress(deliveryAddressRequestDto);

        if(deliveryAddress == null) {
            return new ResponseEntity<>(
                    new CommonResDto<>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "배송지 주소 추가 실패",
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto<Long> resDto
                = new CommonResDto<>(HttpStatus.CREATED, "배송지 주소 추가 성공", deliveryAddress.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }

    @Operation(summary = "배송지 조회")
    @GetMapping("/list-address")
    public ResponseEntity<?> listDeliveryAddress(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo) {

        log.info("listDeliveryAddress =============> ");

        // 인증된 사용자 정보 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        DeliveryAddressResponseDto deliveryAddressResponseDto
                = deliveryService.getDeliveryAddressList(tokenUserInfo.getId());

        CommonResDto<DeliveryAddressResponseDto> resDto
                = new CommonResDto<>(HttpStatus.OK, "배송지 조회 성공", deliveryAddressResponseDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);

    }

    @Operation(summary = "배송 상태 등록")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PostMapping("/status")
    public ResponseEntity<?> addDeliveryStatus(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @Valid @RequestBody DeliveryStatusRequestDto deliveryStatusRequestDto) {

        log.info("DeliveryStatusRequestDto: {}", deliveryStatusRequestDto);

        // 인증된 사용자 정보 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        Delivery delivery = deliveryService.addDeliveryStatus(deliveryStatusRequestDto);

        if(delivery == null) {
            return new ResponseEntity<>(
                    new CommonResDto<>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "배송지 상태 추가 실패",
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto<Long> resDto
                = new CommonResDto<>(HttpStatus.CREATED, "배송지 상태 추가 성공", delivery.getId());

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }

    @Operation(summary = "배송 상태 조회")
    @GetMapping("/check-status/{orderId}")
    public ResponseEntity<?> addDeliveryStatus(
            @AuthenticationPrincipal TokenUserInfo tokenUserInfo,
            @Valid @PathVariable Long orderId) {

        log.info("orderId: {}", orderId);

        // 인증된 사용자 정보 확인
        if (tokenUserInfo == null) {
            return new ResponseEntity<>(new CommonResDto<>(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized",
                    null), HttpStatus.UNAUTHORIZED);
        }

        Delivery.Vo vo = deliveryService.checkDeliveryStatus(orderId);

        if(vo == null) {
            return new ResponseEntity<>(
                    new CommonResDto<>(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "배송 상태 조회 실패",
                            null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResDto<Delivery.Vo> resDto
                = new CommonResDto<>(HttpStatus.CREATED, "배송지 상태 조회 성공", vo);

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);

    }


}
