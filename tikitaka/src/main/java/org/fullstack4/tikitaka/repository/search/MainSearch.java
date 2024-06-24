package org.fullstack4.tikitaka.repository.search;

import org.fullstack4.tikitaka.domain.QuizEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MainSearch {
    Page<QuizEntity> search(Pageable pageable, String search_word, String school, String grade, String semester, String subject, String chapter, String mediumChapter, String chaxi, String share, String status);
}
