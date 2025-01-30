package com.buysellgo.qnaservice.repository;

import com.buysellgo.qnaservice.entity.QnaReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<QnaReply, Long> {
}
