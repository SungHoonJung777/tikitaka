package org.fullstack4.tikitaka.service;

import org.fullstack4.tikitaka.dto.*;

import java.util.List;

public interface MainServiceIf {
    List<QuizDTO> mainListByLike();
    List<QuizDTO> mainListByRegDate();

    PageResponseDTO<QuizDTO> list(PageRequestDTO pageRequestDTO, String order);
    void insertLike(int quizIdx, int memberIdx);
    void deleteLike(int quizIdx, int memberIdx);
    List<Integer> likeList(int memberIdx);

    //초등
    List<ClassDTO> subjectList(String grade, String semester);
    List<LargeChapterDTO> largeChapterList(int classIdx);
    List<MediumChapterDTO> mediumChapterList(int largeIdx);
    List<SmallChapterDTO> smallChapterList(int mediumIdx);

    //중등 고등
    List<ClassDTO> mSubjectList();
    List<ClassDTO> hSubjectList();
}
