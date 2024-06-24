package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.LargeChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LargeChapterRepository extends JpaRepository<LargeChapterEntity, Integer> {

    @Query("SELECT DISTINCT l.largeIdx FROM LargeChapterEntity l JOIN ClassEntity c ON l.classIdx = c.classIdx WHERE (:school IS NULL OR c.school = :school) AND (:grade IS NULL OR c.grade = :grade) AND (:semester IS NULL OR c.semester = :semester) AND (:subject IS NULL OR c.subject = :subject)")
    List<Integer> findLargeChapters(String school, String grade, String semester, String subject);

    List<LargeChapterEntity> findByClassIdx(Integer classIdx);
}
