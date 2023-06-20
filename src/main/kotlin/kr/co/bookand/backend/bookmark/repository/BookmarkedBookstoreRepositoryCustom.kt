package kr.co.bookand.backend.bookmark.repository


import kr.co.bookand.backend.bookmark.domain.Bookmark
import kr.co.bookand.backend.bookmark.domain.BookmarkedBookstore
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BookmarkedBookstoreRepositoryCustom {
    fun findAllByBookmarkAndAndVisibilityTrue(
        bookmark: Bookmark,
        pageable: Pageable,
        cursorId: Long?,
        createdAt: String?
    ): Page<BookmarkedBookstore>

}