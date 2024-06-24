package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_detail_mul")
public class DetailMulEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int mulIdx;
    @Column(length = 500, nullable = true)
    private String mulImg;
    @Column(length = 500, nullable = true)
    private String mulTitle;
    @Column(length = 500, nullable = true)
    private int detailIdx;
}
