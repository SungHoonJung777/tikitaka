package org.fullstack4.tikitaka.service;

import org.fullstack4.tikitaka.dto.MemberDTO;

public interface LoginServiceIf {
    MemberDTO memberInfo(String memberId);
}
