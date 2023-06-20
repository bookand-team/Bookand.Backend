package kr.co.bookand.backend.article.repository

import kr.co.bookand.backend.article.domain.Article
import kr.co.bookand.backend.common.Status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ArticleRepositoryCustom {

    fun findAllBySearch(search: String?, category: String?, status: String?, pageable: Pageable): Page<Article>
    fun findAllByStatus(status: Status?, pageable: Pageable, cursorId: Long?, date: String?): Page<Article>
}