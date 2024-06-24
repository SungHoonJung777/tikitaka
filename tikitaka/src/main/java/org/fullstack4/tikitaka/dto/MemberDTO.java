package org.fullstack4.tikitaka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private int memberIdx;
    private String id;
    private String pwd;
    private String name;
    private String phone;
    private String email;
    private String img;

}
