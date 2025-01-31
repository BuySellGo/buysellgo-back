package com.buysellgo.qnaservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;     
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import jakarta.persistence.EntityManager;
import com.buysellgo.qnaservice.entity.QnaReply;
import com.buysellgo.qnaservice.entity.Qna;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private QnaRepository qnaRepository;
    @Autowired
    private EntityManager entityManager;




    @BeforeEach 
    void setUp() {
        replyRepository.deleteAll();
        qnaRepository.deleteAll();
    }


    @Test
    @DisplayName("Reply를 생성해본다.")
    void createReply() {    
        Qna qna = qnaRepository.save(Qna.of(1L, 1L, 1L, true, "질문")); 
        replyRepository.save(QnaReply.of(qna, 1L, "답변"));
        assertEquals(1, replyRepository.findAll().size());
    }



    @Test
    @DisplayName("Reply를 조회해본다.")
    void getReply() {               
        createReply();
        QnaReply reply = replyRepository.findAll().get(0);
        assertEquals("답변", reply.getContent());
    }

    @Test
    @DisplayName("Reply를 수정해본다.")     
    void updateReply() {
        createReply();
        QnaReply reply = replyRepository.findAll().get(0);
        reply.setContent("수정된 답변");
        replyRepository.save(reply);
        assertEquals("수정된 답변", reply.getContent());

    }   

    @Test
    @DisplayName("Reply를 삭제해본다.")
    void deleteReply() {    
        createReply();
        replyRepository.deleteAll();
        assertEquals(0, replyRepository.findAll().size());
    }

    @Test
    @DisplayName("Reply를 생성하고 Qna를 삭제해본다.")
    void deleteQnaWithReply() {
        // Qna 저장하여 영속 상태로 만든다
        Qna qna = Qna.of(1L, 1L, 1L, true, "질문");
        qnaRepository.save(qna);  // Qna를 영속 상태로 저장

        // Qna가 저장된 후에 QnaReply 저장
        QnaReply reply = QnaReply.of(qna, 1L, "답변");
        replyRepository.save(reply);  // QnaReply를 저장

        // 영속성 컨텍스트에 반영 (DB에 반영되지는 않음)
        entityManager.flush();  // 변경사항을 DB에 반영
        entityManager.clear();  // 영속성 컨텍스트 초기화
        // Qna 삭제
        qnaRepository.deleteAll();  // Qna 삭제



        // 트랜잭션 커밋 후 DB에서 삭제된 상태를 확인
        assertEquals(0, qnaRepository.findAll().size());  // Qna 삭제 확인
        assertEquals(0, replyRepository.findAll().size());  // QnaReply 삭제 확인
    }





}