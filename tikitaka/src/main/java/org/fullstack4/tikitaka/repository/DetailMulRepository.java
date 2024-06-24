package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.DetailBlankEntity;
import org.fullstack4.tikitaka.domain.DetailMulEntity;
import org.fullstack4.tikitaka.domain.QuizReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailMulRepository extends JpaRepository<DetailMulEntity, Integer> {
    DetailMulEntity findByDetailIdx(int detailIdx);
    void deleteByDetailIdx(int detailIdx);
}
