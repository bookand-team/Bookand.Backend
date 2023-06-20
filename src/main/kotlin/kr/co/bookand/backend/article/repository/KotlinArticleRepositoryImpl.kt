package kr.co.bookand.backend.article.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.article.domain.KotlinArticleCategory
import kr.co.bookand.backend.article.domain.QKotlinArticle
import kr.co.bookand.backend.article.domain.QKotlinArticle.*
import kr.co.bookand.backend.common.KotlinStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.time.LocalDateTime

class KotlinArticleRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : KotlinArticleRepositoryCustom {
    override fun findAllBySearch(
        search: String?,
        category: String?,
        status: String?,
        pageable: Pageable
    ): Page<KotlinArticle> {
        val query = queryFactory.selectFrom(kotlinArticle)
        .where(containSearch(search))
            .where(eqCategory(category))
            .where(eqStatus(status))
            .orderBy(kotlinArticle.id.desc())
        return PageableExecutionUtils.getPage(
            query.offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable,
            query::fetchCount)
    }

    override fun findAllByStatus(
        status: KotlinStatus?,
        pageable: Pageable,
        cursorId: Long?,
        date: String?
    ): Page<KotlinArticle> {
        val query = queryFactory.selectFrom(kotlinArticle)
            .where(kotlinArticle.status.eq(status),
                getCursorId(date, cursorId))
            .orderBy(kotlinArticle.createdAt.desc(), kotlinArticle.id.desc())
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
        return kotlinArticle.title.contains(search)
    }

    private fun eqCategory(category: String?): BooleanExpression? {
        if (category.isNullOrEmpty()) {
            return null
        }
        val articleCategory = KotlinArticleCategory.valueOf(category)
        return kotlinArticle.category.eq(articleCategory)
    }

    private fun eqStatus(status: String?): BooleanExpression? {
        if (status.isNullOrEmpty()) {
            return null
        }
        val statusType = KotlinStatus.valueOf(status)
        return kotlinArticle.status.eq(statusType)
    }

    private fun getCursorId(date: String?, cursorId: Long?): BooleanExpression? {
        return if (cursorId == null || cursorId == 0L) {
            null
        } else {
            val parse = LocalDateTime.parse(date)
            kotlinArticle.createdAt.lt(parse)
                .and(kotlinArticle.id.gt(cursorId))
                .or(kotlinArticle.createdAt.lt(parse))
        }
    }

}