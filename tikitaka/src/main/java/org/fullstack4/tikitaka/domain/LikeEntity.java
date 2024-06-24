package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_like")
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int likeIdx;

    @Column(nullable = true)
    private int quizIdx;

    @Column(nullable = true)
    private int memberIdx;
}
