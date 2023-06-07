package kr.co.bookand.backend.article.repository

import kr.co.bookand.backend.article.domain.KotlinArticle
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinArticleRepository : JpaRepository<KotlinArticle, Long> {
    fun existsByTitle(title: String): Boolean
}