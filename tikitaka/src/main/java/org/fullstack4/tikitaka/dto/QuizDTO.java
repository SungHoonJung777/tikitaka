package org.fullstack4.tikitaka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizDTO {
    private int quizIdx;
    private String title;
    private String comment;
    private String thumbnail;
    private String keyword;
    private String school;
    private String grade;
    private String semester;
    private String chapter;
    private String mediumChapter;
    private String chaxi;
    private String orgfile;
    private String savefile;
    private String regDate;
    private String status;
    private String share;
    private String memberIdx;
    private int likeCnt;
    private String name;
    private String subject;
    private int gameIng;
    private int gameEnd;
}
