package org.fullstack4.tikitaka.service;

import org.fullstack4.tikitaka.dto.QuizDetailDTO;

import java.util.List;

public interface QuizDetailServiceIf {
    List<QuizDetailDTO> findAllByQuizIdx(int quizIdx);
    int registDetail(QuizDetailDTO quizDetailDTO);
    void saveDetailData(QuizDetailDTO quizDetailDTO);
    QuizDetailDTO detailQuizNum(int quizDetailIdx);
//    List<QuizDetailDTO> selectJoinDetail(int quizDetailIdx);
    void deleteDetailQuiz(int detailIdx);
}
