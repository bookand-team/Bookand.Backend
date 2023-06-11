package kr.co.bookand.backend.bookmark.repository

import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkedArticle
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinBookmarkedArticleRepository : JpaRepository<KotlinBookmarkedArticle, Long> {
    fun findByBookmarkIdAndArticleId(bookmarkId: Long, articleId: Long): KotlinBookmarkedArticle?
    fun existsByBookmarkIdAndArticleId(bookmarkId: Long, articleId: Long): Boolean
    fun existsByBookmarkIdAndArticleIdAndAccountId(bookmarkId: Long, articleId: Long, accountId: Long): Boolean
    fun deleteByArticleIdAndBookmarkId(articleId: Long, bookmarkId: Long)
    fun findFirstByBookmarkId(bookmarkId: Long): KotlinBookmarkedArticle?
}