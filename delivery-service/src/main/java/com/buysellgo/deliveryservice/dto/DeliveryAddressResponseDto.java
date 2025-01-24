package com.buysellgo.deliveryservice.dto;

import com.buysellgo.deliveryservice.entity.DeliveryAddress;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAddressResponseDto {

    private List<DeliveryAddress> deliveryAddressList;
    private Long deliveryAddressCount;

}
