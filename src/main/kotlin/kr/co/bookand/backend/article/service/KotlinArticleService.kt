package kr.co.bookand.backend.article.service

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.article.domain.KotlinArticleTag
import kr.co.bookand.backend.article.domain.KotlinIntroducedBookstore
import kr.co.bookand.backend.article.domain.dto.*
import kr.co.bookand.backend.article.repository.KotlinArticleRepository
import kr.co.bookand.backend.article.repository.KotlinArticleTagRepository
import kr.co.bookand.backend.article.repository.KotlinIntroducedBookstoreRepository
import kr.co.bookand.backend.bookmark.domain.BookmarkType
import kr.co.bookand.backend.bookmark.service.KotlinBookmarkService
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.bookstore.domain.dto.KotlinBookstoreSimpleResponse
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreRepository
import kr.co.bookand.backend.common.KotlinErrorCode
import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)

class KotlinArticleService(
    private val kotlinArticleRepository: KotlinArticleRepository,
    private val kotlinBookstoreRepository: KotlinBookstoreRepository,
    private val kotlinIntroducedBookstoreRepository: KotlinIntroducedBookstoreRepository,
    private val kotlinArticleTagRepository: KotlinArticleTagRepository,
    private val kotlinBookmarkService: KotlinBookmarkService
) {
    @Transactional
    fun createArticle(
        currentAccount: KotlinAccount,
        kotlinArticleRequest: KotlinArticleRequest
    ): KotlinArticleIdResponse {
        currentAccount.role.checkAdminAndManager()
        duplicateArticle(kotlinArticleRequest.title)

        val bookStoreList = kotlinArticleRequest.bookStoreList
        val articleTags = kotlinArticleRequest.tagList
        val article = KotlinArticle(kotlinArticleRequest)
        val saveArticle = kotlinArticleRepository.save(article)

        addBookstoresToArticle(saveArticle, bookStoreList)
        addArticleTags(saveArticle, articleTags)

        return KotlinArticleIdResponse(saveArticle.id)
    }

    @Transactional
    fun updateArticle(
        currentAccount: KotlinAccount,
        articleId: Long,
        kotlinArticleRequest: KotlinArticleRequest
    ): KotlinArticleIdResponse {
        currentAccount.role.checkAdminAndManager()

        val article = getArticle(articleId)
        article.updateArticleData(kotlinArticleRequest)

        removeIntroducedBookstores(article)
        addBookstoresToArticle(article, kotlinArticleRequest.bookStoreList)
        removeArticleTags(article)
        addArticleTags(article, kotlinArticleRequest.tagList)

        return KotlinArticleIdResponse(article.id)
    }

    fun getArticleList(currentAccount: KotlinAccount, pageable: Pageable?, cursorId: Long?): KotlinArticlePageResponse {
        var nextCursorId = cursorId ?: 0L

        if (cursorId != null && cursorId == 0L) {
            val firstArticle = kotlinArticleRepository.findFirstByStatusOrderByCreatedAtDesc(KotlinStatus.VISIBLE)
                ?: throw RuntimeException(KotlinErrorCode.NOT_FOUND_ARTICLE.errorMessage)
            nextCursorId = firstArticle.id
        }
        val date: String? = if (cursorId == null) null else getArticle(nextCursorId).createdAt.toString()
        val articlePageResponse: Page<KotlinArticleResponse> = kotlinArticleRepository
            .findAllByStatus(KotlinStatus.VISIBLE, pageable, nextCursorId, date)
            .map { article ->
                KotlinArticleResponse(
                    id = article.id,
                    title = article.title,
                    subTitle = article.subTitle,
                    mainImage = article.mainImage,
                    category = article.category,
                    writer = article.writer,
                    status = article.status,
                    view = article.viewCount,
                    isBookmark = kotlinBookmarkService.checkBookmark(
                        currentAccount.id,
                        article.id,
                        BookmarkType.ARTICLE.name
                    ),
                    articleTagList = article.articleTagList.map { it.tag },
                    visibility = article.visibility,
                    createdDate = article.createdAt.toString(),
                    modifiedDate = article.modifiedAt.toString()
                )
            }

        val totalElements = kotlinArticleRepository.countAllByStatus(KotlinStatus.VISIBLE)
        return KotlinArticlePageResponse.of(articlePageResponse, totalElements)
    }

    fun getWebArticleList(currentAccount: KotlinAccount, pageable: Pageable): KotlinWebArticlePageResponse {
        val articlePage = kotlinArticleRepository.findAll(pageable)
            .map(::KotlinWebArticleResponse)
        return KotlinWebArticlePageResponse.of(articlePage)

    }

    fun searchArticleList(
        search: String?,
        category: String?,
        status: String?,
        pageable: Pageable?
    ): KotlinWebArticlePageResponse {
        val articlePage = kotlinArticleRepository.findAllBySearch(search, category, status, pageable)
            .map(::KotlinWebArticleResponse)
        return KotlinWebArticlePageResponse.of(articlePage)
    }


    fun getArticleInfo(currentAccount: KotlinAccount, id: Long): KotlinArticleDetailResponse {
        val article = getArticle(id)

        val bookmarkTypeArticle = BookmarkType.ARTICLE.name
        val bookmarkTypeBookstore = BookmarkType.BOOKSTORE.name
        val accountId = currentAccount.id
        val articleId = article.id

        val bookmarkedArticle = kotlinBookmarkService.checkBookmark(accountId, articleId, bookmarkTypeArticle)
        val articleTagList = article.articleTagList.map { it.tag }
        val bookStoreList = article.introducedBookstoreList.map { introducedBookstore ->
            val bookstore = introducedBookstore.bookstore
            val bookstoreId = bookstore.id
            val bookmarkedBookstore = kotlinBookmarkService.checkBookmark(accountId, bookstoreId, bookmarkTypeBookstore)

            KotlinBookstoreSimpleResponse(
                id = bookstoreId,
                name = bookstore.name,
                introduction = bookstore.introduction,
                mainImage = bookstore.mainImage,
                themeList = bookstore.themeList.map { it.theme.toString() },
                isBookmark = bookmarkedBookstore
            )
        }

        val filter = KotlinArticleFilter(
            deviceOS = article.deviceOSFilter,
            memberId = article.memberIdFilter
        )


        return KotlinArticleDetailResponse(
            id = article.id,
            title = article.title,
            subTitle = article.subTitle,
            mainImage = article.mainImage,
            content = article.content,
            category = article.category,
            writer = article.writer,
            status = article.status,
            view = article.viewCount,
            bookmark = bookmarkedArticle,
            visibility = article.visibility,
            articleTagList = articleTagList,
            bookStoreList = bookStoreList,
            filter = filter,
            createdDate = article.createdAt.toString(),
            modifiedDate = article.modifiedAt.toString(),
            displayDate = article.displayedAt
        )
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
    fun updateArticleStatus(articleId: Long): KotlinMessageResponse {
        val article = getArticle(articleId)
        val status = article.status
        if (status == KotlinStatus.VISIBLE) article.updateArticleStatus(KotlinStatus.INVISIBLE)
        else article.updateArticleStatus(KotlinStatus.VISIBLE)
        return KotlinMessageResponse(message = "SUCCESS", statusCode = 200)
    }

    fun removeArticle(currentAccount: KotlinAccount, articleId: Long) {
        currentAccount.role.checkAdminAndManager()
        softDeleteArticle(getArticle(articleId))
    }

    fun removeArticleList(currentAccount: KotlinAccount, list: KotlinArticleListRequest) {
        currentAccount.role.checkAdminAndManager()
        list.articleIdList.forEach { articleId ->
            softDeleteArticle(getArticle(articleId))
        }
    }

    fun softDeleteArticle(article: KotlinArticle){
        removeIntroducedBookstores(article)
        removeArticleTags(article)
        article.softDelete()
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
            introducedBookstore.bookstore.removeIntroducedBookstore(introducedBookstore)
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