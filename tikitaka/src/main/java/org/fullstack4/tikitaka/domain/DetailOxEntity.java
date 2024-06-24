package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_detail_ox")
public class DetailOxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int oxIdx;
    @Column(length = 500, nullable = true)
    private String ocomment;
    @Column(length = 500, nullable = true)
    private String xcomment;
    @Column(length = 500, nullable = true)
    private int detailIdx;
}
