package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.QuizReportEntity;
import org.fullstack4.tikitaka.dto.QuizReportDTO;
import org.fullstack4.tikitaka.dto.QuizRoomDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuizReportRepository extends JpaRepository<QuizReportEntity, Integer> {
    @Query(value = "select * from tiki_report WHERE room_idx = :roomNumber and student_name = :nickname", nativeQuery = true)
    List<QuizReportEntity> resultStudent(String nickname, int roomNumber);
    List<QuizReportEntity> findStudentQuestionByRoomIdx(int roomIdx);
    @Query(value = "SELECT COUNT(*) as student_count FROM (SELECT DISTINCT student_name FROM tiki_report WHERE room_idx = :roomIdx) tb1", nativeQuery = true)
    int studentcount(int roomIdx);
    int countByRoomIdxAndStudentAnswerYNAndAndStudentQuestion(int roomIdx, String studentAnswerYN, String question);
    @Query(value = "SELECT DISTINCT student_name FROM tiki_report WHERE room_idx = :roomIdx", nativeQuery = true)
    List<String> studentNames(int roomIdx);
    Optional<QuizReportEntity> findByRoomIdxAndStudentNameAndStudentQuestion(int roomIdx, String studentName, String studentQuestion);
    @Query(value = "SELECT DISTINCT student_question FROM tiki_report WHERE room_idx = :roomIdx", nativeQuery = true)
    List<String> question(int roomIdx);
//    Optional<QuizReportEntity> findByRoomIdxAndStudentNameAndStudentQuestion(int roomIdx, String studentName, String question);
}
