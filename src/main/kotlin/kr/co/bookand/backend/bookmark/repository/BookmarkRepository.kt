package kr.co.bookand.backend.bookmark.repository

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.bookmark.model.Bookmark
import kr.co.bookand.backend.bookmark.model.BookmarkType
import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkRepository : JpaRepository<Bookmark, Long> {
    fun findAllByAccountAndBookmarkTypeAndVisibilityTrue(
        account: Account,
        bookmarkType: BookmarkType
    ): List<Bookmark>

    fun findByAccountIdAndFolderNameAndBookmarkTypeAndVisibilityTrue(
        accountId: Long,
        folderName: String,
        bookmarkType: BookmarkType
    ): Bookmark?

    fun findByAccountAndFolderNameAndBookmarkTypeAndVisibilityTrue(
        account: Account,
        folderName: String,
        bookmarkType: BookmarkType
    ): Bookmark?

    fun findByIdAndAccountIdAndVisibilityTrue(bookmarkId: Long, accountId: Long): Bookmark?

    fun findAllByAccountId(accountId: Long): List<Bookmark>

    fun countAllByVisibility(visibility: Boolean): Long

    fun countAllByVisibilityAndBookmarkType(visibility: Boolean, bookmarkType: BookmarkType): Long
}