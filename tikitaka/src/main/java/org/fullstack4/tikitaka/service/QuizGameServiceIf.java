package org.fullstack4.tikitaka.service;



import org.fullstack4.tikitaka.dto.*;

import java.util.List;

public interface QuizGameServiceIf {
    public QuizRoomDTO createRoom(QuizRoomDTO quizRoomDTO);
    public QuizReportDTO saveReport(QuizReportDTO quizReportDTO);
    public QuizMemberDTO saveQuizMember(QuizMemberDTO quizMemberDTO);

    public QuizDTO readQuiz(QuizDTO quizDTO);
    public List<QuizDetailDTO> readQuizDetailList(QuizDTO quizDTO);

    public QuizRoomDTO readRoom(QuizRoomDTO quizRoomDTO);

    public List<QuizMemberDTO> readRanking(QuizRoomDTO quizRoomDTO);
    public void modifyroom(QuizRoomDTO quizRoomDTO);
}

