package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_quizmember")
@EntityListeners(value={AuditingEntityListener.class})
public class QuizMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int quizMemberIdx;
    @Column(length = 11, nullable = true)
    private int roomIdx;
    @Column(length = 20, nullable = true)
    private String studentName;
    @Column(length = 4, nullable = true)
    private String studentPassword;
    @Column(length = 200, nullable = true)
    private String studentImgUrl;
    @Column(length = 2, nullable = true)
    private int studentCorrectCount;
    @Column(length = 2, nullable = true)
    private int studentAnswerCount;
    @Column(length = 4, nullable = true)
    private int studentTotalScore;
    @CreatedDate
    @Column(name="reg_date", updatable = true)
    private LocalDateTime regDate;

    public void modifyScore(int studentCorrectCount, int studentAnswerCount, int studentTotalScore){
        this.studentCorrectCount = studentCorrectCount;
        this.studentAnswerCount = studentAnswerCount;
        this.studentTotalScore = studentTotalScore;

    }
}
