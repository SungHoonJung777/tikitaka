package org.fullstack4.tikitaka.service;

import org.fullstack4.tikitaka.domain.QuizEntity;
import org.fullstack4.tikitaka.domain.QuizRoomEntity;
import org.fullstack4.tikitaka.dto.QuizDTO;
import org.fullstack4.tikitaka.dto.QuizMemberDTO;
import org.fullstack4.tikitaka.dto.QuizReportDTO;
import org.fullstack4.tikitaka.dto.QuizRoomDTO;

import java.util.List;

public interface ReportServiceIf {
    List<QuizRoomDTO> list(int makeUser, String status, String search_word);
    int enterCount(int roomIdx);
    int reportCount(int teacherIdx);
    QuizRoomDTO Roominfo(int roomIdx);
    String quizdanwon(int quizIdx);
    QuizDTO quizinfo(int quizIdx);
    List<QuizReportDTO> quizReport(int quizIdx);
    List<QuizReportDTO> questiondetail(int roomIdx, String question);
    List<QuizMemberDTO> memberscore(int roomIdx);
    List<QuizReportDTO> memberdetail(int roomIdx, String studentName);
    void endreport(int roomIdx);
    void deletereport(String roomIdx);
}
