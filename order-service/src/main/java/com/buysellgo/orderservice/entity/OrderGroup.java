package com.buysellgo.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_order_group")
@Entity
public class OrderGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_group_seq")
    @SequenceGenerator(
        name = "order_group_seq",
        sequenceName = "order_group_seq",
        initialValue = 1,
        allocationSize = 1
    )
    private Long groupId;
}
