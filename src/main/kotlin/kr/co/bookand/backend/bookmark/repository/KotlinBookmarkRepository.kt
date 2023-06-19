package kr.co.bookand.backend.bookmark.repository

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.bookmark.domain.Bookmark
import kr.co.bookand.backend.bookmark.domain.BookmarkType
import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface KotlinBookmarkRepository : JpaRepository<KotlinBookmark, Long> {
    fun findAllByAccountAndBookmarkType(
        account: KotlinAccount,
        bookmarkType: KotlinBookmarkType
    ): List<KotlinBookmark>

    fun findByAccountIdAndFolderNameAndBookmarkType(
        accountId: Long,
        folderName: String,
        bookmarkType: KotlinBookmarkType
    ): KotlinBookmark?

    fun findByAccountAndFolderNameAndBookmarkType(
        account: KotlinAccount,
        folderName: String,
        bookmarkType: KotlinBookmarkType
    ): KotlinBookmark?

    fun findByIdAndAccountId(bookmarkId: Long, accountId: Long): KotlinBookmark?

    fun findAllByAccountId(accountId: Long): List<KotlinBookmark>

    fun countAllByVisibility(visibility: Boolean): Long

    fun countAllByVisibilityAndBookmarkType(visibility: Boolean, bookmarkType: KotlinBookmarkType): Long
}