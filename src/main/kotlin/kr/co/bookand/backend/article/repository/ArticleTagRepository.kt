package kr.co.bookand.backend.article.repository

import kr.co.bookand.backend.article.domain.ArticleTag
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleTagRepository : JpaRepository<ArticleTag, Long> {

}