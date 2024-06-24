package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_large_chapter")
public class LargeChapterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int largeIdx;
    @Column(length = 500, nullable = true)
    private int classIdx;
    @Column(length = 500, nullable = true)
    private String largeChapter;
}