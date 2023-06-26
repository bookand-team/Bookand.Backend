package kr.co.bookand.backend.article.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.article.model.Article
import kr.co.bookand.backend.article.model.ArticleCategory
import kr.co.bookand.backend.article.model.QArticle.*
import kr.co.bookand.backend.common.Status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.time.LocalDateTime

class ArticleRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ArticleRepositoryCustom {
    override fun findAllBySearch(
        search: String?,
        category: String?,
        status: String?,
        pageable: Pageable
    ): Page<Article> {
        val query = queryFactory.selectFrom(article)
        .where(containSearch(search))
            .where(eqCategory(category))
            .where(eqStatus(status))
            .orderBy(article.id.desc())
        return PageableExecutionUtils.getPage(
            query.offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable,
            query::fetchCount)
    }

    override fun findAllByStatus(
        status: Status?,
        pageable: Pageable,
        cursorId: Long?,
        date: String?
    ): Page<Article> {
        val query = queryFactory.selectFrom(article)
            .where(article.status.eq(status),
                getCursorId(date, cursorId))
            .orderBy(article.createdAt.desc(), article.id.desc())
        return PageableExecutionUtils.getPage(
            query.offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable,
            query::fetchCount)
    }

    private fun containSearch(search: String?): BooleanExpression? {
        if (search.isNullOrEmpty()) {
            return null
        }
        return article.title.contains(search)
    }

    private fun eqCategory(category: String?): BooleanExpression? {
        if (category.isNullOrEmpty()) {
            return null
        }
        val articleCategory = ArticleCategory.valueOf(category)
        return article.category.eq(articleCategory)
    }

    private fun eqStatus(status: String?): BooleanExpression? {
        if (status.isNullOrEmpty()) {
            return null
        }
        val statusType = Status.valueOf(status)
        return article.status.eq(statusType)
    }

    private fun getCursorId(date: String?, cursorId: Long?): BooleanExpression? {
        return if (cursorId == null || cursorId == 0L) {
            null
        } else {
            val parse = LocalDateTime.parse(date)
            article.createdAt.lt(parse)
                .and(article.id.gt(cursorId))
                .or(article.createdAt.lt(parse))
        }
    }

}