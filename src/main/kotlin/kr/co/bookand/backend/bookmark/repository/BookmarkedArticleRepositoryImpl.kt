package kr.co.bookand.backend.bookmark.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.bookmark.domain.Bookmark
import kr.co.bookand.backend.bookmark.domain.BookmarkedArticle
import kr.co.bookand.backend.bookmark.domain.QBookmarkedArticle.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.time.LocalDateTime

class BookmarkedArticleRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : BookmarkedArticleRepositoryCustom {
    override fun findAllByBookmarkAndAndVisibilityTrue(
        bookmark: Bookmark,
        pageable: Pageable,
        cursorId: Long?,
        createdAt: String?
    ): Page<BookmarkedArticle> {

        val query = queryFactory.selectFrom(bookmarkedArticle)
            .where(
                bookmarkedArticle.bookmark.id.eq(bookmark.id),
                getCursorId(createdAt, cursorId)
            )
            .orderBy(bookmarkedArticle.createdAt.desc(), bookmarkedArticle.id.desc())
        return PageableExecutionUtils.getPage(
            query.orderBy(bookmarkedArticle.createdAt.desc(), bookmarkedArticle.id.desc())
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
            bookmarkedArticle.createdAt.lt(date)
                .and(bookmarkedArticle.id.gt(cursorId))
                .or(bookmarkedArticle.createdAt.lt(date))
        }
    }
}
