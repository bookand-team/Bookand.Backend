package kr.co.bookand.backend.bookmark.repository


import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkedBookstore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface KotlinBookmarkedBookstoreRepositoryCustom {
    fun findAllByBookmarkAndAndVisibilityTrue(
        bookmark: KotlinBookmark,
        pageable: Pageable,
        cursorId: Long?,
        createdAt: String?
    ): Page<KotlinBookmarkedBookstore>

}