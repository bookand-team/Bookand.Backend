package kr.co.bookand.backend.bookmark.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkedBookstore
import kr.co.bookand.backend.bookmark.domain.QKotlinBookmarkedBookstore
import kr.co.bookand.backend.bookmark.domain.QKotlinBookmarkedBookstore.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.time.LocalDateTime

class KotlinBookmarkedBookstoreRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : KotlinBookmarkedBookstoreRepositoryCustom {

    override fun findAllByBookmarkAndAndVisibilityTrue(
        bookmark: KotlinBookmark,
        pageable: Pageable,
        cursorId: Long?,
        createdAt: String?
    ): Page<KotlinBookmarkedBookstore> {

        val query = queryFactory.selectFrom(kotlinBookmarkedBookstore)
            .where(
                kotlinBookmarkedBookstore.bookmark.id.eq(bookmark.id),
                getCursorId(createdAt, cursorId)
            )
            .orderBy(kotlinBookmarkedBookstore.createdAt.desc(), kotlinBookmarkedBookstore.id.desc())
        return PageableExecutionUtils.getPage(
            query.orderBy(kotlinBookmarkedBookstore.createdAt.desc(), kotlinBookmarkedBookstore.id.desc())
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
            kotlinBookmarkedBookstore.createdAt.lt(date)
                .and(kotlinBookmarkedBookstore.id.gt(cursorId))
                .or(kotlinBookmarkedBookstore.createdAt.lt(date))
        }
    }
}