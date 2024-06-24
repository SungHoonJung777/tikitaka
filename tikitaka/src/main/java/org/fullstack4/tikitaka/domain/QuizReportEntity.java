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
@Table(name="tiki_report")
@EntityListeners(value={AuditingEntityListener.class})
public class QuizReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int reportIdx;
    @Column(length = 11, nullable = true)
    private int roomIdx;
    @Column(length = 20, nullable = true)
    private String studentName;
    @Column(length = 500, nullable = true)
    private String studentQuestion;
    @Column(length = 200, nullable = true)
    private String studentAnswer;
    @Column(length = 1, nullable = true)
    private String studentAnswerYN;
    @Column(length = 3, nullable = true)
    private int studentScore;
    @CreatedDate
    @Column(name="reg_date", updatable = true)
    private LocalDateTime regDate;
}
