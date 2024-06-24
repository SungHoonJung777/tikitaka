package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.DetailBlankEntity;
import org.fullstack4.tikitaka.domain.DetailDescEntity;
import org.fullstack4.tikitaka.domain.QuizReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailDescRepository extends JpaRepository<DetailDescEntity, Integer> {
    DetailDescEntity findByDetailIdx(int detailIdx);
    void deleteByDetailIdx(int detailIdx);
}
