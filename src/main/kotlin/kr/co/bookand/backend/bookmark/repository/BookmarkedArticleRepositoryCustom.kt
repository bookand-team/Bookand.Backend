package kr.co.bookand.backend.bookmark.repository

import kr.co.bookand.backend.bookmark.model.Bookmark
import kr.co.bookand.backend.bookmark.model.BookmarkedArticle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BookmarkedArticleRepositoryCustom {

    fun findAllByBookmarkAndAndVisibilityTrue(
        bookmark: Bookmark,
        pageable: Pageable,
        cursorId: Long?,
        createdAt: String?
    ): Page<BookmarkedArticle>
}