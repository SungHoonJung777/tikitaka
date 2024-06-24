package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.MediumChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MediumChapterRepository extends JpaRepository<MediumChapterEntity, Integer> {

    @Query("SELECT DISTINCT m.mediumIdx FROM MediumChapterEntity m WHERE m.largeIdx = :largeIdx")
    List<Integer> findMediumChapters(int largeIdx);

    List<MediumChapterEntity> findByLargeIdx(int largeIdx);
}
