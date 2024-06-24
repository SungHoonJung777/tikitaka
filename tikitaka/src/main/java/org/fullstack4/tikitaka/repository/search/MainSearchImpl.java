package org.fullstack4.tikitaka.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.fullstack4.tikitaka.domain.QQuizEntity;
import org.fullstack4.tikitaka.domain.QuizEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class MainSearchImpl extends QuerydslRepositorySupport implements MainSearch {

    public MainSearchImpl() {
        super(QuizEntity.class);
    }

    @Override
    public Page<QuizEntity> search(Pageable pageable, String search_word, String school, String grade, String semester, String subject, String chapter, String mediumChapter, String chaxi, String share, String status) {
        QQuizEntity qQuizEntity = QQuizEntity.quizEntity;
        JPQLQuery<QuizEntity> query = from(qQuizEntity);

        BooleanBuilder builder = new BooleanBuilder();
        if (search_word != null && !search_word.isEmpty()) {
            builder.and(qQuizEntity.title.contains(search_word));
        }
        if (school != null && !school.isEmpty()) {
            builder.and(qQuizEntity.school.equalsIgnoreCase(school));
        }
        if (subject != null && !subject.isEmpty()) {
            builder.and(qQuizEntity.subject.equalsIgnoreCase(subject));
        }
        if (grade != null && !grade.isEmpty()) {
            builder.and(qQuizEntity.grade.equalsIgnoreCase(grade));
        }
        if (semester != null && !semester.isEmpty()) {
            builder.and(qQuizEntity.semester.equalsIgnoreCase(semester));
        }
        if (chapter != null && !chapter.isEmpty()) {
            builder.and(qQuizEntity.chapter.equalsIgnoreCase(chapter));
        }
        if (mediumChapter != null && !mediumChapter.isEmpty()) {
            builder.and(qQuizEntity.chapter.equalsIgnoreCase(mediumChapter));
        }
        if (chaxi != null && !chaxi.isEmpty()) {
            builder.and(qQuizEntity.chaxi.equalsIgnoreCase(chaxi));
        }
        if (share != null && !share.isEmpty()) {
            builder.and(qQuizEntity.share.equalsIgnoreCase(share));
        }
        if (status != null && !status.isEmpty()) {
            builder.and(qQuizEntity.status.contains(status));
        }

        query.where(builder);

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<QuizEntity> result = query.fetch();
        int total = (int)query.fetchCount();

        return new PageImpl<>(result, pageable, total);
    }
}
