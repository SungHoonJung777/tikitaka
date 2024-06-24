package org.fullstack4.tikitaka.service;

import org.fullstack4.tikitaka.dto.QuizDetailDTO;
import org.fullstack4.tikitaka.dto.QuizReportDTO;
import org.fullstack4.tikitaka.dto.TogetherDetailDTO;

import java.util.List;

public interface TogetherService {
    List<TogetherDetailDTO> readTogetherDetailList(int quizIdx);

    int insertRoom(String code,String roomName, String nickname , int roomIdx , int memberIdx);

    int insertScore(QuizReportDTO quizReportDTO);
    int modifyScore(QuizReportDTO quizReportDTO);

    int modifyStatusRoom(int roomNumber);
    int modifyStatusRoomEnd(int roomNumber);

    String getRoomStatus(int roomNumber);

    int getCountY(String question, int roomNumber);

    List<QuizReportDTO> resultStudent(String nickname,int roomNumber);

    int getQuizIdx(int roomIdx);
}
