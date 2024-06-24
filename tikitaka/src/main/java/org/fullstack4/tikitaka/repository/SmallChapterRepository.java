package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.SmallChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SmallChapterRepository extends JpaRepository<SmallChapterEntity, Integer> {

    @Query("SELECT DISTINCT s.smallIdx FROM SmallChapterEntity s WHERE s.mediumIdx = :mediumIdx")
    List<Integer> findSmallChapters(int mediumIdx);

    List<SmallChapterEntity> findByMediumIdx(int mediumIdx);
}
