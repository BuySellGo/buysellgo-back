package com.buysellgo.promotionservice.repository;

import com.buysellgo.promotionservice.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    @Query("""
            SELECT p.userId, COUNT(p.orderId)
            FROM PaymentHistory p
            WHERE p.paidAt BETWEEN :startDate AND :endDate
            GROUP BY p.userId
            HAVING COUNT(p.orderId) >= 3
            """)
    List<Object[]> findUsersWithMoreThanTenOrdersThisMonth(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate
    );
}
