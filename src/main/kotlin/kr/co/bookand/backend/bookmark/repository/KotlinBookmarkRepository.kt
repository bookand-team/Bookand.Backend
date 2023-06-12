package kr.co.bookand.backend.bookmark.repository

import kr.co.bookand.backend.bookmark.domain.BookmarkType
import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinBookmarkRepository : JpaRepository<KotlinBookmark, Long> {

    fun findByAccountIdAndFolderNameAndBookmarkType(
        accountId: Long,
        folderName: String,
        bookmarkType: BookmarkType
    ): KotlinBookmark?

    fun findByIdAndAccountId(bookmarkId: Long, accountId: Long): KotlinBookmark?

    fun findByAccountIdAndBookmarkType(accountId: Long, bookmarkType: BookmarkType): List<KotlinBookmark>

    fun findAllByAccountId(accountId: Long): List<KotlinBookmark>

    fun countAllByVisibility(visibility: Boolean): Long

    fun countAllByVisibilityAndBookmarkType(visibility: Boolean, bookmarkType: BookmarkType): Long
}