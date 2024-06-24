package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {

    @Query("SELECT DISTINCT c.subject FROM ClassEntity c WHERE (:school IS NULL OR c.school = :school) AND (:grade IS NULL OR c.grade = :grade) AND (:semester IS NULL OR c.semester = :semester)")
    List<String> findSubjects(String school, String grade, String semester);

    List<ClassEntity> findBySchoolAndGradeAndSemester(String school, String grade, String semester);
    List<ClassEntity> findSubjectBySchool(String school);
}
