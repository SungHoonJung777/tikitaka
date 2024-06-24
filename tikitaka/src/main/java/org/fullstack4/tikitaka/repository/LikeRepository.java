package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    @Query(value = "SELECT quiz_idx FROM tiki_like WHERE member_idx = :memberIdx", nativeQuery = true)
    List<Integer> getLikeByMemberId(@Param("memberIdx") int memberIdx);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tiki_like (quiz_idx, member_idx) VALUES (:quizIdx, :memberIdx)", nativeQuery = true)
    void insertLike(@Param("quizIdx") int quizIdx, @Param("memberIdx") int memberIdx);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tiki_like WHERE quiz_idx = :quizIdx AND member_idx = :memberIdx", nativeQuery = true)
    void deleteLike(@Param("quizIdx") int quizIdx, @Param("memberIdx") int memberIdx);

    void deleteByQuizIdx(int quizIdx);
}
