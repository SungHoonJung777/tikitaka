package org.fullstack4.tikitaka.service;

import lombok.RequiredArgsConstructor;
import org.fullstack4.tikitaka.dto.MemberDTO;
import org.fullstack4.tikitaka.repository.LoginRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginServiceIf{
    private final ModelMapper modelMapper;
    private final LoginRepository loginRepository;

    @Override
    public MemberDTO memberInfo(String memberId) {
        MemberDTO memberInfo = modelMapper.map(loginRepository.findByMemberId(memberId), MemberDTO.class);
        return memberInfo;
    }
}
