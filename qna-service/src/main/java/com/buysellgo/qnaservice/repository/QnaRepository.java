package com.buysellgo.qnaservice.repository;

import com.buysellgo.qnaservice.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {
}
