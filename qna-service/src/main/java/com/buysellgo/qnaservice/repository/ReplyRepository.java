package com.buysellgo.qnaservice.repository;

import com.buysellgo.qnaservice.entity.QnaReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<QnaReply, Long> {
}

