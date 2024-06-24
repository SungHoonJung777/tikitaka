package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_room")
@EntityListeners(value={AuditingEntityListener.class})
public class QuizRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int roomIdx;
    @Column(length = 11, nullable = true)
    private int teacherIdx;
    @Column(length = 20, nullable = true)
    private String teacherName;
    @Column(length = 200, nullable = true)
    private String title;
    @Column(length = 500, nullable = true)
    private String intro;
    @Column(length = 1, nullable = true)
    private String commentYN;
    @Column(length = 1, nullable = true)
    private String scoreYN;
    @Column(length = 1, nullable = true)
    private String continueYN;
    @Column(length = 1, nullable = true)
    private String rankYN;
    @Column(length = 1, nullable = true)
    private String bMusicYN;
    @Column(length = 1, nullable = true)
    private String eMusicYN;
    @Column(length = 11, nullable = true)
    private int quizIdx;
    @CreatedDate
    @Column(name="reg_date", updatable = true)
    private LocalDateTime regDate;
    @Column
    private LocalDate endDate;
    @Column(length = 1, nullable = true)
    private String status;
    @Column(length = 10, nullable = true)
    private String type;

    public void modifystatus(String status){
        this.status = status;
    }
}
