package kr.co.bookand.backend.bookmark.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkedArticle
import kr.co.bookand.backend.bookmark.domain.QKotlinBookmarkedArticle
import kr.co.bookand.backend.bookmark.domain.QKotlinBookmarkedArticle.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.time.LocalDateTime

class KotlinBookmarkedArticleRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : KotlinBookmarkedArticleRepositoryCustom {
    override fun findAllByBookmarkAndAndVisibilityTrue(
        bookmark: KotlinBookmark,
        pageable: Pageable,
        cursorId: Long?,
        createdAt: String?
    ): Page<KotlinBookmarkedArticle> {

        val query = queryFactory.selectFrom(kotlinBookmarkedArticle)
            .where(
                kotlinBookmarkedArticle.bookmark.id.eq(bookmark.id),
                getCursorId(createdAt, cursorId)
            )
            .orderBy(kotlinBookmarkedArticle.createdAt.desc(), kotlinBookmarkedArticle.id.desc())
        return PageableExecutionUtils.getPage(
            query.orderBy(kotlinBookmarkedArticle.createdAt.desc(), kotlinBookmarkedArticle.id.desc())
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
            kotlinBookmarkedArticle.createdAt.lt(date)
                .and(kotlinBookmarkedArticle.id.gt(cursorId))
                .or(kotlinBookmarkedArticle.createdAt.lt(date))
        }
    }
}
