package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.DetailBlankEntity;
import org.fullstack4.tikitaka.domain.DetailOxEntity;
import org.fullstack4.tikitaka.domain.QuizReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailOxRepository extends JpaRepository<DetailOxEntity, Integer> {
    DetailOxEntity findByDetailIdx(int detailIdx);
    void deleteByDetailIdx(int detailIdx);
}
