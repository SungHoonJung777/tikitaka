package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_detail_blank")
public class DetailBlankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int blankIdx;
    @Column(length = 500, nullable = true)
    private String blank;
    @Column(length = 500, nullable = true)
    private int detailIdx;
}
