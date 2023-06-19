package kr.co.bookand.backend.article.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.common.KotlinStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


class KotlinArticleRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : KotlinArticleRepositoryCustom {
    override fun findAllBySearch(
        search: String?,
        category: String?,
        status: String?,
        pageable: Pageable?
    ): Page<KotlinArticle> {
        TODO("Not yet implemented")
    }

    override fun findAllByStatus(
        status: KotlinStatus?,
        pageable: Pageable?,
        cursorId: Long?,
        date: String?
    ): Page<KotlinArticle> {
        TODO("Not yet implemented")
    }
}