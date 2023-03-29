package kr.co.bookand.backend.article.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.bookand.backend.article.domain.Article;
import kr.co.bookand.backend.article.domain.ArticleCategory;
import kr.co.bookand.backend.common.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static kr.co.bookand.backend.article.domain.QArticle.*;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Article> findAllBySearch(String search, String category, String status, Pageable pageable) {
        JPQLQuery<Article> query = queryFactory.selectFrom(article)
                .where(containSearch(search))
                .where(eqCategory(category))
                .where(eqStatus(status))
                .orderBy(article.id.desc());

        return PageableExecutionUtils.getPage(
                query.offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                query::fetchCount);
    }

    @Override
    public Page<Article> findAllByStatus(Status status, Pageable pageable, Long cursorId, String date) {
        JPQLQuery<Article> query = queryFactory.selectFrom(article)
                .where(article.status.eq(status),
                        getCursorId(date, cursorId))
                .orderBy(article.createdAt.desc(), article.id.desc());

        return PageableExecutionUtils.getPage(
                query.offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                query::fetchCount);
    }

    private BooleanExpression containSearch(String search) {
        if(search == null || search.isEmpty()) {
            return null;
        }
        return article.title.contains(search);
    }

    private BooleanExpression eqCategory(String category) {
        if(category == null || category.isEmpty()) {
            return null;
        }
        ArticleCategory articleCategory = ArticleCategory.valueOf(category);
        return article.category.eq(articleCategory);
    }

    private BooleanExpression eqStatus(String status) {
        if(status == null || status.isEmpty()) {
            return null;
        }
        Status statusType = Status.valueOf(status);
        return article.status.eq(statusType);
    }

    private BooleanExpression getCursorId(String date, Long cursorId) {
        return cursorId == null || cursorId == 0 ? null : article.createdAt.lt(date)
                .and(article.id.gt(cursorId))
                .or(article.createdAt.lt(date));
    }
}
