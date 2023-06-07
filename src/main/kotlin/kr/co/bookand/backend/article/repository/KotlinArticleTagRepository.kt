package kr.co.bookand.backend.article.repository

import kr.co.bookand.backend.article.domain.KotlinArticleTag
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinArticleTagRepository : JpaRepository<KotlinArticleTag, Long> {

}