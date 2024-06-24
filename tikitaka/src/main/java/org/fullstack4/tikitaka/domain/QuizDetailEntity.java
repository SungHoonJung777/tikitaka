package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_quizdetail")
public class QuizDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int detailIdx;
    @Column(length = 500, nullable = true)
    private String category;
    @Column(length = 500, nullable = true)
    private int orders;
    @Column(length = 500,nullable = true)
    private int timer;
    @Column(length = 500,nullable = true)
    private int score;
    @Column(length = 500, nullable = true)
    private String question;
    @Column(length = 500,nullable = true)
    private String comment;
    @Column(length = 500, nullable = true)
    private String answer;
    @Column(length = 500, nullable = true)
    private int quizIdx;
    @Column(length = 500, nullable = true)
    private String media;
    @Column(length = 500, nullable = true)
    private String youtubeUrl;
}
