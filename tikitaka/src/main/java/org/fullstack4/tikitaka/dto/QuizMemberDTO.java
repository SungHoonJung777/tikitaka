package org.fullstack4.tikitaka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizMemberDTO {
    private int quizMemberIdx;
    private int roomIdx;
    private String studentName;
    private String studentPassword;
    private String studentImgUrl;
    private int studentCorrectCount;
    private int studentAnswerCount;
    private int studentTotalScore;
    private LocalDateTime regDate;

    private String startEnd;
    private int userCount;
    private String enterDate;
}
