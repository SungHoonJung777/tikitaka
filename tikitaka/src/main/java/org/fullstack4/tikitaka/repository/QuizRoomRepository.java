package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.QuizRoomEntity;
import org.fullstack4.tikitaka.dto.QuizRoomDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizRoomRepository extends JpaRepository<QuizRoomEntity, Integer> {
    List<QuizRoomEntity> findByTeacherIdxAndStatusAndTitleContainsOrderByRoomIdx(int makeUser, String status, String search_word);
    int countAllByTeacherIdx(int teacherIdx);
    QuizRoomEntity findByRoomIdx(int roomIdx);
    int countByQuizIdxAndTypeAndStatus(int quizIdx, String type, String status);
    QuizRoomEntity findAllByRoomIdx(int roomIdx);
    @Query(value = "DELETE FROM tiki_room WHERE room_idx in (:roomIdx)", nativeQuery = true)
    void deleteroom(String roomIdx);
}
