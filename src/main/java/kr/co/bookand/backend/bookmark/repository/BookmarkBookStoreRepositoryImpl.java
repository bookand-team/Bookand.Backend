package kr.co.bookand.backend.bookmark.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.bookand.backend.bookmark.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static kr.co.bookand.backend.bookmark.domain.QBookmarkBookStore.*;

@RequiredArgsConstructor
public class BookmarkBookStoreRepositoryImpl implements BookmarkBookStoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookmarkBookStore> findAllByBookmarkAndAndVisibilityTrue(
            Bookmark bookmark,
            Pageable pageable,
            Long cursorId,
            String createdAt
    ) {
        JPAQuery<BookmarkBookStore> query = queryFactory
                .selectFrom(bookmarkBookStore)
                .where(
                        bookmarkBookStore.bookmark.id.eq(bookmark.getId()),
                        getCursorId(createdAt, cursorId)
                )
                .orderBy(bookmarkBookStore.createdAt.desc(), bookmarkBookStore.id.desc());

        return PageableExecutionUtils.getPage(
                query.orderBy(bookmarkBookStore.createdAt.desc(), bookmarkBookStore.id.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                () -> queryFactory.selectFrom(bookmarkBookStore)
                        .fetch().size());
    }

    private BooleanExpression getCursorId(String createdAt, Long cursorId) {
        return cursorId == null || cursorId == 0 ? null : bookmarkBookStore.createdAt.lt(createdAt)
                .and(bookmarkBookStore.id.gt(cursorId))
                .or(bookmarkBookStore.createdAt.lt(createdAt));
    }
}
