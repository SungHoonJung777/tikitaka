package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_class")
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int classIdx;
    @Column(length = 500, nullable = true)
    private String school;
    @Column(length = 500, nullable = true)
    private String grade;
    @Column(length = 500, nullable = true)
    private String semester;
    @Column(length = 500, nullable = true)
    private String subject;
}