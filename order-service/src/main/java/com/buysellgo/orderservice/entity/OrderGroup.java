package com.buysellgo.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import com.buysellgo.orderservice.common.entity.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_order_group")
@Entity
public class OrderGroup extends BaseEntity{ 
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_group_seq")
    @SequenceGenerator(
        name = "order_group_seq",
        sequenceName = "order_group_seq",
        initialValue = 1,
        allocationSize = 1
    )
    private Long groupId;

    @Column(name = "user_id", columnDefinition = "bigint", nullable = true, unique = false)
    private Long userId;

    public static OrderGroup of(long userId){
        return OrderGroup.builder()
                .userId(userId)
                .build();
    }

    public Vo toVo(){
        return new Vo(groupId, userId, version, createdAt, updatedAt);
    }
    

    public record Vo(Long groupId, Long userId, long version, Timestamp createdAt, Timestamp updatedAt) {}

}
