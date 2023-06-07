package kr.co.bookand.backend.article.service

import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.article.domain.KotlinArticleTag
import kr.co.bookand.backend.article.domain.KotlinIntroducedBookstore
import kr.co.bookand.backend.article.domain.dto.KotlinArticleRequest
import kr.co.bookand.backend.article.domain.dto.KotlinArticleResponse
import kr.co.bookand.backend.article.domain.dto.KotlinIntroducedBookstoreRequest
import kr.co.bookand.backend.article.domain.dto.toResponse
import kr.co.bookand.backend.article.repository.KotlinArticleRepository
import kr.co.bookand.backend.article.repository.KotlinArticleTagRepository
import kr.co.bookand.backend.article.repository.KotlinIntroducedBookstoreRepository
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreRepository
import kr.co.bookand.backend.common.domain.Status
import lombok.RequiredArgsConstructor
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.locks.ReentrantLock
import javax.persistence.LockModeType

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

class KotlinArticleService(
    private val kotlinArticleRepository: KotlinArticleRepository,
    private val kotlinAccountService: KotlinAccountService,
    private val kotlinBookstoreRepository: KotlinBookstoreRepository,
    private val kotlinIntroducedBookstoreRepository: KotlinIntroducedBookstoreRepository,
    private val kotlinArticleTagRepository: KotlinArticleTagRepository
) {
    @Transactional
    fun createArticle(id: Long, kotlinArticleRequest: KotlinArticleRequest): KotlinArticleResponse {
        kotlinAccountService.checkAccountAdmin(id)
        duplicateArticle(kotlinArticleRequest.title)

        val bookStoreList = kotlinArticleRequest.bookStoreList
        val articleTags = kotlinArticleRequest.tagList

        val article = KotlinArticle(kotlinArticleRequest)
        val saveArticle = kotlinArticleRepository.save(article)

        addBookstoresToArticle(saveArticle, bookStoreList)
        addArticleTags(saveArticle, articleTags)

        return saveArticle.toResponse()
    }

    @Transactional
    fun updateArticle(id: Long, articleId: Long, kotlinArticleRequest: KotlinArticleRequest): KotlinArticleResponse {
        kotlinAccountService.checkAccountAdmin(id)

        val article = getArticle(articleId)
        article.updateArticleData(kotlinArticleRequest)

        removeIntroducedBookstores(article)
        addBookstoresToArticle(article, kotlinArticleRequest.bookStoreList)
        removeArticleTags(article)
        addArticleTags(article, kotlinArticleRequest.tagList)

        return article.toResponse()
    }


    private fun addBookstoresToArticle(article: KotlinArticle, bookstoreList: List<Long>) {
        bookstoreList.map { getBookstore(it) }.toList().forEach { bookstore ->
            val introducedBookstore = KotlinIntroducedBookstore(KotlinIntroducedBookstoreRequest(article, bookstore))
            val savedIntroducedBookstore = kotlinIntroducedBookstoreRepository.save(introducedBookstore)
            article.updateIntroducedBookstore(savedIntroducedBookstore)
            bookstore.updateIntroducedBookstore(savedIntroducedBookstore)
        }
    }


    private fun addArticleTags(article: KotlinArticle, tagList: List<String>) {
        tagList.map { KotlinArticleTag(tag = it) }.forEach { articleTag ->
            val savedArticleTag = kotlinArticleTagRepository.save(articleTag)
            article.updateArticleTag(savedArticleTag)
            savedArticleTag.updateArticle(article)
        }
    }


    @Transactional
    fun updateArticleStatus(articleId: Long, status: Status): KotlinArticle {
        val article = getArticle(articleId)
        article.updateArticleStatus(status)
        return article
    }

    fun removeArticle(accountId: Long, articleId: Long) {
        kotlinAccountService.checkAccountAdmin(accountId)
        val article = getArticle(articleId)
        removeIntroducedBookstores(article)
        removeArticleTags(article)
        article.softDelete()
    }

    fun removeArticleList(accountId: Long, articleIdList: List<Long>) {
        kotlinAccountService.checkAccountAdmin(accountId)
        articleIdList.forEach { articleId ->
            val article = getArticle(articleId)
            removeIntroducedBookstores(article)
            removeArticleTags(article)
            article.softDelete()
        }
    }

    fun duplicateArticle(title: String) {
        if (isDuplicateArticle(title)) {
            throw RuntimeException("DUPLICATE ARTICLE")
        }
    }

    fun getBookstore(bookstoreId: Long): KotlinBookstore {
        return kotlinBookstoreRepository.findById(bookstoreId)
            .orElseThrow { RuntimeException("NOT FOUND BOOKSTORE") }
    }

    fun getArticle(articleId: Long): KotlinArticle {
        return kotlinArticleRepository.findById(articleId)
            .orElseThrow { RuntimeException("NOT FOUND ARTICLE") }
    }

    private fun removeIntroducedBookstores(article: KotlinArticle) {
        article.introducedBookstoreList.toList().forEach { introducedBookstore ->
            kotlinIntroducedBookstoreRepository.delete(introducedBookstore)
            introducedBookstore.bookStore.removeIntroducedBookstore(introducedBookstore)
            article.removeIntroducedBookstore(introducedBookstore)
        }
    }

    private fun removeArticleTags(article: KotlinArticle) {
        article.articleTagList.toList().forEach { articleTag ->
            kotlinArticleTagRepository.delete(articleTag)
            article.removeArticleTag(articleTag)
        }
    }

    private fun isDuplicateArticle(title: String): Boolean {
        return kotlinArticleRepository.existsByTitle(title)
    }
}