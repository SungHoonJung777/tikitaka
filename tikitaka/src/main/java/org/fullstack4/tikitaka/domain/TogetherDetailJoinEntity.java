package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_together")
public class TogetherDetailJoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int detailIdx;
    @Column(length = 500, nullable = true)
    private String answer;
    @Column(length = 500, nullable = true)
    private String category;
    @Column(length = 500,nullable = true)
    private String comment;
    @Column(length = 500, nullable = true)
    private String question;
    @Column(length = 500, nullable = true)
    private int quizIdx;
    @Column(length = 500,nullable = true)
    private int score;
    @Column(length = 500,nullable = true)
    private int timer;
    @Column(length = 500, nullable = true)
    private String media;
    @Column(length = 500, nullable = true)
    private String youtubeUrl;




    @Column(length = 500, nullable = false)
    private String descMin;
    @Column(length = 500, nullable = true)
    private String ocomment;
    @Column(length = 500, nullable = true)
    private String xcomment;
    @Column(length = 500, nullable = true)
    private String mulImg;
    @Column(length = 500, nullable = true)
    private String mulTitle;
    @Column(length = 500, nullable = true)
    private String blank;
}
