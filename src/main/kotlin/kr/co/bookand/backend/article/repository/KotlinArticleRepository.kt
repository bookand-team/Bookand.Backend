package kr.co.bookand.backend.article.repository

import kr.co.bookand.backend.article.domain.ArticleCategory
import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.common.domain.Status
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinArticleRepository : JpaRepository<KotlinArticle, Long> {
    fun existsByTitle(title: String): Boolean

    fun countAllByVisibility(visibility: Boolean): Long
    fun countAllByVisibilityAndCategory(visibility: Boolean, articleCategory: ArticleCategory): Long
    fun countAllByStatus(status: Status): Long
}