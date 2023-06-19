package kr.co.bookand.backend.bookmark.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkedArticle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class KotlinBookmarkedArticleRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : KotlinBookmarkedArticleRepositoryCustom {
    override fun findAllByBookmarkAndAndVisibilityTrue(
        bookmark: KotlinBookmark,
        pageable: Pageable?,
        cursorId: Long?,
        createdAt: String?
    ): Page<KotlinBookmarkedArticle> {
        TODO("Not yet implemented")
    }
}