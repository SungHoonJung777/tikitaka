package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.QuizMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizMemberRepository extends JpaRepository<QuizMemberEntity, Integer> {

    QuizMemberEntity findByStudentNameAndStudentPasswordAndRoomIdx(String studentName, String studentPassword,int roomIdx);
    int countByRoomIdx(int roomIdx);
    List<QuizMemberEntity> findByRoomIdxOrderByStudentTotalScoreDesc(int roomIdx);
    List<QuizMemberEntity> findByRoomIdx(int roomIdx);
}
