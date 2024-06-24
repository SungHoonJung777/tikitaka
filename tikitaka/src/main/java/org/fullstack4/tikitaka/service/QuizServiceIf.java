package org.fullstack4.tikitaka.service;

import org.fullstack4.tikitaka.dto.PageRequestDTO;
import org.fullstack4.tikitaka.dto.PageResponseDTO;
import org.fullstack4.tikitaka.dto.QuizDTO;

import java.util.List;

public interface QuizServiceIf {
    int regist(QuizDTO quizDTO);
    int modify(QuizDTO quizDTO);
    void delete(int quizIdx);

    QuizDTO view(Integer quizIdx);
    PageResponseDTO<QuizDTO> pagelist(PageRequestDTO pageRequestDTO, String order, Integer member_idx);
    PageResponseDTO<QuizDTO> pagelikelist(PageRequestDTO pageRequestDTO, String order, Integer member_idx);

    int countGameStatus(Integer quizIdx, String status);
    void updateLikeCnt(int quizIdx, int likeCnt);

    int tmpSaveQuizDetail(int quizIdx);
    int SaveQuiz(int quizIdx);
}
