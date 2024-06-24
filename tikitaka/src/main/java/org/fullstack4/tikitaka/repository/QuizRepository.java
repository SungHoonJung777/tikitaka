package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.QuizEntity;
import org.fullstack4.tikitaka.repository.search.QuizSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<QuizEntity, Integer>, JpaSpecificationExecutor<QuizEntity>, QuizSearch {
    Optional<QuizEntity> findByQuizIdx(Integer quizIdx);
    QuizEntity findByQuizIdx(int quizIdx);

    @Modifying
    @Transactional
    @Query(value ="UPDATE tiki_quiz SET like_cnt = :likeCnt WHERE quiz_idx = :quizIdx", nativeQuery = true)
    void updateLikeCntByQuizIdx(int quizIdx, int likeCnt);

    @Modifying
    @Transactional
    @Query(value = "update tiki_quiz set status = 'T' where quiz_idx =:quizIdx", nativeQuery = true)
    int updateTmpStatus(int quizIdx);
    @Modifying
    @Transactional
    @Query(value = "update tiki_quiz set status = 'S' where quiz_idx =:quizIdx", nativeQuery = true)
    int saveTmpStatus(int quizIdx);


}
