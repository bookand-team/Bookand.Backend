package kr.co.bookand.backend.bookmark.repository

import kr.co.bookand.backend.bookmark.domain.Bookmark
import kr.co.bookand.backend.bookmark.domain.BookmarkBookStore
import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkedBookstore
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinBookmarkedBookstoreRepository : JpaRepository<KotlinBookmarkedBookstore, Long>, KotlinBookmarkedBookstoreRepositoryCustom {
    fun findByBookmarkIdAndBookstoreId(bookmarkId: Long, bookstoreId: Long): KotlinBookmarkedBookstore?
    fun existsByBookmarkIdAndBookstoreId(bookmarkId: Long, bookstoreId: Long): Boolean
    fun existsByBookmarkIdAndBookstoreIdAndAccountId(bookmarkId: Long, bookstoreId: Long, accountId: Long): Boolean
    fun deleteByBookstoreIdAndBookmarkId(bookstoreId: Long, bookmarkId: Long)
    fun findFirstByBookmarkId(bookmarkId: Long): KotlinBookmarkedBookstore?
    fun countAllByBookmark(bookmark: KotlinBookmark): Long
    fun findAllByBookmark(bookmark: KotlinBookmark): List<KotlinBookmarkedBookstore>
}