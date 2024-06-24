package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_small_chapter")
public class SmallChapterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int smallIdx;
    @Column(length = 500, nullable = true)
    private int mediumIdx;
    @Column(length = 500, nullable = true)
    private String smallChapter;
}