package kr.co.bookand.backend.article.repository

import kr.co.bookand.backend.article.model.Article
import kr.co.bookand.backend.article.model.ArticleCategory
import kr.co.bookand.backend.common.Status
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Long>, ArticleRepositoryCustom {
    fun existsByTitle(title: String): Boolean
    fun findFirstByStatusOrderByCreatedAtDesc(status: Status): Article?
    fun countAllByVisibility(visibility: Boolean): Long
    fun countAllByVisibilityAndCategory(visibility: Boolean, articleCategory: ArticleCategory): Long
    fun countAllByStatus(status: Status): Long
}