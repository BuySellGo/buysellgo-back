package com.buysellgo.qnaservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;     
import com.buysellgo.qnaservice.entity.Qna;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QnaRepositoryTest {
    @Autowired
    private QnaRepository qnaRepository;

    @BeforeEach
    void setUp() {
        qnaRepository.deleteAll();
    }

    @Test
    @DisplayName("Qna를 생성해본다.")
    void createQna() {
        qnaRepository.save(Qna.of(1L, 1L, 1L, true, "질문"));
        assertEquals(1, qnaRepository.findAll().size());
    }



    @Test
    @DisplayName("Qna를 조회해본다.")
    void getQna() {
        createQna();
        Qna qna = qnaRepository.findAll().get(0);
        assertEquals("질문", qna.getContent());
    }


    @Test
    @DisplayName("Qna를 수정해본다.")
    void updateQna() {
        createQna();
        Qna qna = qnaRepository.findAll().get(0);
        qna.setContent("수정된 질문");
        qnaRepository.save(qna);
        assertEquals("수정된 질문", qna.getContent());
    }


    @Test
    @DisplayName("Qna를 삭제해본다.")
    void deleteQna() {
        createQna();
        Qna qna = qnaRepository.findAll().get(0);
        qnaRepository.delete(qna);
        assertEquals(0, qnaRepository.findAll().size());
    }

}