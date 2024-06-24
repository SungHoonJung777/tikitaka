package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_quiz")
public class QuizEntity  extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int quizIdx;
    @Column(length = 500, nullable = true)
    private String title;
    @Column(length = 500, nullable = true)
    private  String thumbnail;
    @Column(length = 500, nullable = true)
    private String keyword;
    @Column(length = 500, nullable = true)
    private String school;
    @Column(length = 500, nullable = true)
    private String grade;
    @Column(length = 500, nullable = true)
    private String semester;
    @Column(length = 500, nullable = true)
    private String chapter;
    @Column(length = 500, nullable = true)
    private String mediumChapter;
    @Column(length = 500, nullable = true)
    private String chaxi;
    @Column(length = 500, nullable = true)
    private String orgfile;
    @Column(length = 500, nullable = true)
    private String savefile;
    @Column(length = 500, nullable = true)
    private String status;
    @Column(length = 500, nullable = true)
    private String share;
    @Column(length = 500, nullable = true)
    private int memberIdx;
    @Column(length=500, nullable = true)
    private int likeCnt;
    @Column(length = 500, nullable = true)
    private String name;
    @Column(length = 500, nullable = true)
    private String subject;
    @Column(length = 500, nullable = true)
    private String comment;
}
