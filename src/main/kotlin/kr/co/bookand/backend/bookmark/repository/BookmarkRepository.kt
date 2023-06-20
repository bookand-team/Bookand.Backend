package kr.co.bookand.backend.bookmark.repository

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.bookmark.domain.Bookmark
import kr.co.bookand.backend.bookmark.domain.BookmarkType
import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkRepository : JpaRepository<Bookmark, Long> {
    fun findAllByAccountAndBookmarkType(
        account: Account,
        bookmarkType: BookmarkType
    ): List<Bookmark>

    fun findByAccountIdAndFolderNameAndBookmarkType(
        accountId: Long,
        folderName: String,
        bookmarkType: BookmarkType
    ): Bookmark?

    fun findByAccountAndFolderNameAndBookmarkType(
        account: Account,
        folderName: String,
        bookmarkType: BookmarkType
    ): Bookmark?

    fun findByIdAndAccountId(bookmarkId: Long, accountId: Long): Bookmark?

    fun findAllByAccountId(accountId: Long): List<Bookmark>

    fun countAllByVisibility(visibility: Boolean): Long

    fun countAllByVisibilityAndBookmarkType(visibility: Boolean, bookmarkType: BookmarkType): Long
}