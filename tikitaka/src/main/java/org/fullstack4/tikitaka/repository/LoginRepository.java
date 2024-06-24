package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<MemberEntity, Integer> {
    MemberEntity findByMemberId(String memberId);
}
