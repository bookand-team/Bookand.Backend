package kr.co.bookand.backend.article.repository

import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.common.KotlinStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface KotlinArticleRepositoryCustom {

    fun findAllBySearch(search: String?, category: String?, status: String?, pageable: Pageable): Page<KotlinArticle>
    fun findAllByStatus(status: KotlinStatus?, pageable: Pageable, cursorId: Long?, date: String?): Page<KotlinArticle>
}