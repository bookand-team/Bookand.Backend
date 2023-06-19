package kr.co.bookand.backend.bookmark.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkedBookstore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class KotlinBookmarkedBookstoreRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : KotlinBookmarkedBookstoreRepositoryCustom {

    override fun findAllByBookmarkAndAndVisibilityTrue(
        bookmark: KotlinBookmark,
        pageable: Pageable?,
        cursorId: Long?,
        createdAt: String?
    ): Page<KotlinBookmarkedBookstore> {
        TODO("Not yet implemented")
    }
}