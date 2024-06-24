package org.fullstack4.tikitaka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizReportDTO {
    private int reportIdx;
    private int roomIdx;
    private String studentName;
    private String studentQuestion;
    private String studentAnswer;
    private String studentAnswerYN;
    private int studentScore;
    private LocalDateTime regDate;
    private int student_count;
    private int correct_count;
    private String correct_persent;
}
