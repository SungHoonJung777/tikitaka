package org.fullstack4.tikitaka.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="tiki_member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = true)
    private int memberIdx;
    @Column(length = 500, nullable = true)
    private String memberId;
    @Column(length = 500, nullable = true)
    private String pwd;
    @Column(length = 50, nullable = true)
    private String name;
    @Column(length = 12, nullable = true)
    private String phone;
    @Column(length = 500, nullable = true)
    private String email;
    @Column(length = 500, nullable = true)
    private String img;
}
