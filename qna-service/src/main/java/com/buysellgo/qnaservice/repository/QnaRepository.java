package com.buysellgo.qnaservice.repository;

import com.buysellgo.qnaservice.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
    List<Qna> findByProductId(long productId);
    List<Qna> findByUserId(long userId);
    List<Qna> findBySellerId(long sellerId);
}