package kr.co.bookand.backend.bookmark.repository

import kr.co.bookand.backend.bookmark.domain.Bookmark
import kr.co.bookand.backend.bookmark.domain.BookmarkedBookstore
import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkedBookstoreRepository : JpaRepository<BookmarkedBookstore, Long>, BookmarkedBookstoreRepositoryCustom {
    fun findByBookmarkIdAndBookstoreId(bookmarkId: Long, bookstoreId: Long): BookmarkedBookstore?
    fun existsByBookmarkIdAndBookstoreId(bookmarkId: Long, bookstoreId: Long): Boolean
    fun existsByBookmarkIdAndBookstoreIdAndAccountId(bookmarkId: Long, bookstoreId: Long, accountId: Long): Boolean
    fun deleteByBookstoreIdAndBookmarkId(bookstoreId: Long, bookmarkId: Long)
    fun findFirstByBookmarkId(bookmarkId: Long): BookmarkedBookstore?
    fun countAllByBookmark(bookmark: Bookmark): Long
    fun findAllByBookmark(bookmark: Bookmark): List<BookmarkedBookstore>
}