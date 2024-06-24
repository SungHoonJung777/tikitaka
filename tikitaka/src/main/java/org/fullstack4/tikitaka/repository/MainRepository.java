package org.fullstack4.tikitaka.repository;

import org.fullstack4.tikitaka.domain.QuizEntity;
import org.fullstack4.tikitaka.repository.search.MainSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MainRepository extends JpaRepository<QuizEntity, Integer>, JpaSpecificationExecutor<QuizEntity>, MainSearch {
    List<QuizEntity> findTop4ByShareAndStatusOrderByLikeCntDesc(String share, String status);
    List<QuizEntity> findTop4ByShareAndStatusOrderByRegDateDesc(String share, String status);



}
