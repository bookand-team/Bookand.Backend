package kr.co.bookand.backend.article.repository

import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.article.domain.KotlinArticleCategory
import kr.co.bookand.backend.common.domain.Status
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinArticleRepository : JpaRepository<KotlinArticle, Long>, KotlinArticleRepositoryCustom {
    fun existsByTitle(title: String): Boolean
    fun findFirstByStatusOrderByCreatedAtDesc(status: Status): KotlinArticle?
    fun countAllByVisibility(visibility: Boolean): Long
    fun countAllByVisibilityAndCategory(visibility: Boolean, articleCategory: KotlinArticleCategory): Long
    fun countAllByStatus(status: Status): Long
}