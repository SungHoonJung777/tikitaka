package org.fullstack4.tikitaka.repository.search;

import org.fullstack4.tikitaka.domain.QuizEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuizSearch {
    Page<QuizEntity> search(Pageable pageable, String search_word, Integer member_idx);
    Page<QuizEntity> searchlike(Pageable pageable, String search_word, Integer member_idx);
}
