package com.buysellgo.reviewservice.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.buysellgo.reviewservice.entity.Review;

import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
    }

    @Test
    @DisplayName("리뷰를 생성해본다.")
    void createReview() {
        reviewRepository.save(Review.of(1, 1, 1, 5, "좋아요", "https://example.com/image.jpg"));
        assertEquals(1, reviewRepository.findAll().size());
    }

    @Test
    @DisplayName("리뷰를 조회해본다.")
    void getReview() {
        createReview();
        Review review = reviewRepository.findByUserId(1L).orElseThrow();
        assertEquals(1L, review.getUserId());
    }

    @Test
    @DisplayName("리뷰를 수정해본다.")
    void updateReview() {
        createReview();
        Review review = reviewRepository.findById(1L).orElseThrow();
        review.setContent("좋아요");
        reviewRepository.save(review);
        assertEquals("좋아요", review.getContent());
    }

    @Test
    @DisplayName("리뷰를 삭제해본다.")
    void deleteReview() {
        createReview();
        Review review = reviewRepository.findByUserId(1L).orElseThrow();
        reviewRepository.delete(review);
        assertEquals(0, reviewRepository.findAll().size());
    }
}