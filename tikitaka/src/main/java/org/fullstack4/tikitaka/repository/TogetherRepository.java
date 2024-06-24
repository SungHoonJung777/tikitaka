package org.fullstack4.tikitaka.repository;

import jakarta.transaction.Transactional;
import org.fullstack4.tikitaka.domain.QuizDetailEntity;
import org.fullstack4.tikitaka.domain.QuizReportEntity;
import org.fullstack4.tikitaka.domain.TogetherDetailJoinEntity;
import org.fullstack4.tikitaka.dto.QuizDetailDTO;
import org.fullstack4.tikitaka.dto.QuizReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TogetherRepository extends JpaRepository<TogetherDetailJoinEntity, Integer> {
    /*    @Query(value = "SELECT * FROM (" +
                "SELECT " +
                "tdb.detail_idx AS tdb_detail_idx, " +
                "tdd.detail_idx AS tdd_detail_idx, " +
                "tdo.detail_idx AS tdo_detail_idx, " +
                "tdm.detail_idx AS tdm_detail_idx, " +
                "tq.detail_idx AS tq_detail_idx " +
                "FROM tiki_quizdetail tdb " +
                "LEFT OUTER JOIN tiki_detail_desc tdd ON tdb.detail_idx = tdd.detail_idx " +
                "LEFT OUTER JOIN tiki_detail_ox tdo ON tdb.detail_idx = tdo.detail_idx " +
                "LEFT OUTER JOIN tiki_detail_mul tdm ON tdb.detail_idx = tdm.detail_idx " +
                "LEFT OUTER JOIN tiki_detail_blank tq ON tdb.detail_idx = tq.detail_idx " +
                "WHERE tdb.quiz_idx = :quizIdx" +
                ") AS subquery", nativeQuery = true)
        List<QuizDetailEntity> findAllByQuizIdx(@Param("quizIdx") int quizIdx);*/

    @Query(value = "SELECT tdb.detail_idx ,tdb.answer ,tdb.category ,tdb.comment ,tdb.question ,tdb.quiz_idx ,tdb.score ,tdb.timer ,tdb.media, tdb.youtube_url , tdd.desc_min , tdo.ocomment , tdo.xcomment , tdm.mul_img , tdm.mul_title , tq.blank " +
            "FROM tiki_quizdetail tdb " +
            "LEFT OUTER JOIN tiki_detail_desc tdd ON tdb.detail_idx = tdd.detail_idx " +
            "LEFT OUTER JOIN tiki_detail_ox tdo ON tdb.detail_idx = tdo.detail_idx " +
            "LEFT OUTER JOIN tiki_detail_mul tdm ON tdb.detail_idx = tdm.detail_idx " +
            "LEFT OUTER JOIN tiki_detail_blank tq ON tdb.detail_idx = tq.detail_idx " +
            "WHERE tdb.quiz_idx = :quizIdx order by tdb.detail_idx asc ", nativeQuery = true)
    List<TogetherDetailJoinEntity> findAllByQuizIdx(@Param("quizIdx") int quizIdx);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tiki_room (room_idx, status, teacher_name, title, type, reg_date,quiz_idx , teacher_idx) " +
            "VALUES (:code, 's', :nickname, :roomName, 'together', now() , :roomIdx , :memberIdx)", nativeQuery = true)
    int insertRoom(@Param("code") String code, @Param("roomName") String roomName, @Param("nickname") String nickname, @Param("roomIdx") int roomIdx, @Param("memberIdx") int memberIdx);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO tiki_report (room_idx, student_answer, student_answeryn, student_name, student_question, student_score , reg_date) " +
            "VALUES (:#{#quizReportDTO.roomIdx}, :#{#quizReportDTO.studentAnswer}, :#{#quizReportDTO.studentAnswerYN}, " +
            ":#{#quizReportDTO.studentName}, :#{#quizReportDTO.studentQuestion}, :#{#quizReportDTO.studentScore} , now())", nativeQuery = true)
    int insertScore(QuizReportDTO quizReportDTO);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tiki_report " +
            "SET student_answer = :#{#quizReportDTO.studentAnswer}, " +
            "    student_answeryn = :#{#quizReportDTO.studentAnswerYN}, " +
            "    student_score = :#{#quizReportDTO.studentScore} " +
            "WHERE student_name = :#{#quizReportDTO.studentName}",
            nativeQuery = true)
    int modifyScore(QuizReportDTO quizReportDTO);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tiki_room SET status = 'g' WHERE room_idx = :roomNumber", nativeQuery = true)
    int modifyStatusRoom(@Param("roomNumber") int roomNumber);
    @Modifying
    @Transactional
    @Query(value = "UPDATE tiki_room SET status = 'e' WHERE room_idx = :roomNumber", nativeQuery = true)
    int modifyStatusRoomEnd(@Param("roomNumber") int roomNumber);

    @Query(value = "select status from tiki_room WHERE room_idx = :roomNumber ", nativeQuery = true)
    String getRoomStatus(int roomNumber);
    @Query(value = "select count(*) from tiki_report WHERE room_idx = :roomNumber and student_answeryn = 'y' and student_question = :question", nativeQuery = true)
    int getCountY(String question, int roomNumber );

    @Query(value = "select quiz_idx from tiki_room WHERE room_idx = :roomIdx", nativeQuery = true)
    int getQuizIdx(int roomIdx);
}
