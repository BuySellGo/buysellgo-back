package com.buysellgo.deliveryservice.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 테이블과 관련이 없고, 컬럼 정보만 자식에게 제공하기 위해 사용하는 아노테이션.
public abstract class BaseTimeEntity {

    @Column(name = "created_at", columnDefinition = "timestamp", nullable = false)
    @CreationTimestamp
    protected Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp", nullable = false)
    @UpdateTimestamp
    protected Timestamp updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    protected long version;

}
