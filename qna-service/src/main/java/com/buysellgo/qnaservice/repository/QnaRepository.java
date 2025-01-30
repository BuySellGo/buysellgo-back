package com.buysellgo.qnaservice.repository;

import com.buysellgo.qnaservice.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
}
