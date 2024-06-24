package org.fullstack4.tikitaka.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.fullstack4.tikitaka.domain.QLikeEntity;
import org.fullstack4.tikitaka.domain.QQuizEntity;
import org.fullstack4.tikitaka.domain.QuizEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

public class QuizSearchImpl extends QuerydslRepositorySupport implements QuizSearch {

    public QuizSearchImpl() {
        super(QuizEntity.class);
    }

    @Override
    public Page<QuizEntity> search(Pageable pageable, String search_word, Integer member_idx) {
        QQuizEntity qQuizEntity = QQuizEntity.quizEntity;
        JPQLQuery<QuizEntity> query = from(qQuizEntity);

        BooleanBuilder builder = new BooleanBuilder();
        if (search_word != null && !search_word.isEmpty()) {
            builder.and(qQuizEntity.title.contains(search_word));
        }
        if (member_idx != null || !Objects.isNull(member_idx)) {
            builder.and(qQuizEntity.memberIdx.eq(member_idx));
        }
        query.where(builder);

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<QuizEntity> result = query.fetch();
        int total = (int)query.fetchCount();

        return new PageImpl<>(result, pageable, total);
    }


    @Override
    public Page<QuizEntity> searchlike(Pageable pageable, String searchWord, Integer memberIdx) {
        QQuizEntity qQuizEntity = QQuizEntity.quizEntity;
        QLikeEntity qLikeEntity = QLikeEntity.likeEntity;

        JPQLQuery<QuizEntity> query = from(qQuizEntity)
                .leftJoin(qLikeEntity).on(qQuizEntity.quizIdx.eq(qLikeEntity.quizIdx).and(qLikeEntity.memberIdx.eq(memberIdx)));

        BooleanBuilder builder = new BooleanBuilder();
        if (searchWord != null && !searchWord.isEmpty()) {
            builder.and(qQuizEntity.title.contains(searchWord));
        }
        // 조인된 테이블의 좋아요가 있는 경우만 필터링
        builder.and(qLikeEntity.memberIdx.eq(memberIdx));

        query.where(builder)
                .groupBy(qQuizEntity.quizIdx);

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<QuizEntity> result = query.fetch();
        int total = (int)query.fetchCount();

        return new PageImpl<>(result, pageable, total);
    }
}
