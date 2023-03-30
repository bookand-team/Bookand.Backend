package kr.co.bookand.backend.bookmark.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkArticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static kr.co.bookand.backend.bookmark.domain.QBookmarkArticle.bookmarkArticle;

@Slf4j
@RequiredArgsConstructor
public class BookmarkArticleRepositoryImpl implements BookmarkArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<BookmarkArticle> findAllByBookmarkAndAndVisibilityTrue(
            Bookmark bookmark,
            Pageable pageable,
            Long cursorId,
            String createdAt
    ) {
        JPAQuery<BookmarkArticle> query = queryFactory
                .selectFrom(bookmarkArticle)
                .where(
                        bookmarkArticle.bookmark.id.eq(bookmark.getId()),
                        getCursorId(createdAt, cursorId)
                )
                .orderBy(bookmarkArticle.createdAt.desc(), bookmarkArticle.id.desc());

        return PageableExecutionUtils.getPage(
                query.orderBy(bookmarkArticle.createdAt.desc(), bookmarkArticle.id.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                () -> queryFactory.selectFrom(bookmarkArticle)
                        .fetch().size());

    }

    public BooleanExpression getCursorId(String createdAt, Long cursorId) {
        return cursorId == null || cursorId == 0 ? null : bookmarkArticle.createdAt.lt(createdAt)
                .and(bookmarkArticle.id.gt(cursorId))
                .or(bookmarkArticle.createdAt.lt(createdAt));
    }
}
