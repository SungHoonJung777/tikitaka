package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.QuizDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizDetailRepository extends JpaRepository<QuizDetailEntity, Integer> {
    List<QuizDetailEntity> findAllByQuizIdx(int quizIdx);
    void deleteByQuizIdx(int quizIdx);
    QuizDetailEntity findByDetailIdx(int quizDetailIdx);

    void deleteByDetailIdx(int detailIdx);
}
