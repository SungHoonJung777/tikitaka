package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_detail_desc")
public class DetailDescEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int descIdx;
    @Column(length = 500, nullable = false)
    private String descMin;
    @Column(length = 500, nullable = false)
    private int detailIdx;
}
