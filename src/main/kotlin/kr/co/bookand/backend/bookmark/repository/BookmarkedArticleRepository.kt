package kr.co.bookand.backend.bookmark.repository

import kr.co.bookand.backend.bookmark.domain.Bookmark
import kr.co.bookand.backend.bookmark.domain.BookmarkedArticle
import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkedArticleRepository : JpaRepository<BookmarkedArticle, Long>, BookmarkedArticleRepositoryCustom {
    fun findByBookmarkIdAndArticleId(bookmarkId: Long, articleId: Long): BookmarkedArticle?
    fun existsByBookmarkIdAndArticleId(bookmarkId: Long, articleId: Long): Boolean
    fun existsByBookmarkIdAndArticleIdAndAccountId(bookmarkId: Long, articleId: Long, accountId: Long): Boolean
    fun deleteByArticleIdAndBookmarkId(articleId: Long, bookmarkId: Long)
    fun findFirstByBookmarkId(bookmarkId: Long): BookmarkedArticle?
    fun countAllByBookmark(bookmark: Bookmark): Long
}