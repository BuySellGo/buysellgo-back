package com.buysellgo.deliveryservice.service;

import com.buysellgo.deliveryservice.dto.DeliveryAddressRequestDto;
import com.buysellgo.deliveryservice.dto.DeliveryAddressResponseDto;
import com.buysellgo.deliveryservice.dto.DeliveryStatusRequestDto;
import com.buysellgo.deliveryservice.entity.Delivery;
import com.buysellgo.deliveryservice.entity.DeliveryAddress;
import com.buysellgo.deliveryservice.entity.QDeliveryAddress;
import com.buysellgo.deliveryservice.repository.DeliveryAddressRepository;
import com.buysellgo.deliveryservice.repository.DeliveryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final DeliveryRepository deliveryRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public DeliveryAddress addDeliveryAddress(DeliveryAddressRequestDto deliveryAddressRequestDto) {

        QDeliveryAddress qDeliveryAddress = QDeliveryAddress.deliveryAddress;

        if(deliveryAddressRequestDto.getIsDefault() == Boolean.TRUE){
            long updateCount = jpaQueryFactory.update(qDeliveryAddress)
                    .set(qDeliveryAddress.isDefault, Boolean.FALSE)
                    .where(qDeliveryAddress.userId.eq(deliveryAddressRequestDto.getUserId()))
                    .execute();
            log.info("updateCount: {}", updateCount);
        }

        DeliveryAddress deliveryAddress = deliveryAddressRepository.save(
                DeliveryAddress.of(
                        deliveryAddressRequestDto.getUserId(),
                        deliveryAddressRequestDto.getAddressName(),
                        deliveryAddressRequestDto.getReceiver(),
                        deliveryAddressRequestDto.getPhone(),
                        deliveryAddressRequestDto.getZipCode(),
                        deliveryAddressRequestDto.getAddress(),
                        deliveryAddressRequestDto.getDetailAddress(),
                        deliveryAddressRequestDto.getIsDefault(),
                        deliveryAddressRequestDto.getCreatedAt()
                )
        );

        return deliveryAddress;
    }

    public DeliveryAddressResponseDto getDeliveryAddressList(Long userId) {

        DeliveryAddressResponseDto deliveryAddressResponseDto =
                new DeliveryAddressResponseDto();

        deliveryAddressResponseDto.setDeliveryAddressList(deliveryAddressRepository.findByUserId(userId));

        if(deliveryAddressResponseDto.getDeliveryAddressList().isEmpty()){
            throw new EntityNotFoundException("No delivery address found for userId: " + userId);
        }

        deliveryAddressResponseDto.setDeliveryAddressCount(deliveryAddressRepository.countByUserId(userId));

        return deliveryAddressResponseDto;

    }

    public Delivery addDeliveryStatus(DeliveryStatusRequestDto deliveryStatusRequestDto) {

        Delivery delivery = deliveryRepository.save(Delivery.of(
                deliveryStatusRequestDto.getOrderId(),
                deliveryStatusRequestDto.getCreatedAt(),
                deliveryStatusRequestDto.getDeliveryStatus()
        ));

        return delivery;

    }

    public Delivery.Vo checkDeliveryStatus(Long orderId) {

        Delivery delivery = deliveryRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("No delivery found for orderId: " + orderId));

        return delivery.toVo();
    }
}
