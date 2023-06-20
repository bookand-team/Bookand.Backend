package kr.co.bookand.backend.bookmark.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.bookmark.domain.Bookmark
import kr.co.bookand.backend.bookmark.domain.BookmarkedBookstore
import kr.co.bookand.backend.bookmark.domain.QBookmarkedBookstore.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.time.LocalDateTime

class BookmarkedBookstoreRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : BookmarkedBookstoreRepositoryCustom {

    override fun findAllByBookmarkAndAndVisibilityTrue(
        bookmark: Bookmark,
        pageable: Pageable,
        cursorId: Long?,
        createdAt: String?
    ): Page<BookmarkedBookstore> {

        val query = queryFactory.selectFrom(bookmarkedBookstore)
            .where(
                bookmarkedBookstore.bookmark.id.eq(bookmark.id),
                getCursorId(createdAt, cursorId)
            )
            .orderBy(bookmarkedBookstore.createdAt.desc(), bookmarkedBookstore.id.desc())
        return PageableExecutionUtils.getPage(
            query.orderBy(bookmarkedBookstore.createdAt.desc(), bookmarkedBookstore.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable,
            query::fetchCount
        )
    }

    private fun getCursorId(createdAt: String?, cursorId: Long?): BooleanExpression? {
        return if (cursorId == null || cursorId == 0L) {
            null
        } else {
            val date = LocalDateTime.parse(createdAt)
            bookmarkedBookstore.createdAt.lt(date)
                .and(bookmarkedBookstore.id.gt(cursorId))
                .or(bookmarkedBookstore.createdAt.lt(date))
        }
    }
}