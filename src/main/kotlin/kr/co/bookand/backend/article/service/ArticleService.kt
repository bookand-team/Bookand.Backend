package kr.co.bookand.backend.article.service

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.article.dto.*
import kr.co.bookand.backend.article.model.Article
import kr.co.bookand.backend.article.model.ArticleTag
import kr.co.bookand.backend.article.model.IntroducedBookstore
import kr.co.bookand.backend.article.repository.ArticleRepository
import kr.co.bookand.backend.article.repository.ArticleTagRepository
import kr.co.bookand.backend.article.repository.IntroducedBookstoreRepository
import kr.co.bookand.backend.bookmark.model.BookmarkType
import kr.co.bookand.backend.bookmark.service.BookmarkService
import kr.co.bookand.backend.bookstore.model.Bookstore
import kr.co.bookand.backend.bookstore.dto.BookstoreSimpleResponse
import kr.co.bookand.backend.bookstore.repository.BookstoreRepository
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.common.model.MessageResponse
import kr.co.bookand.backend.common.exception.BookandException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)

class ArticleService(
    private val articleRepository: ArticleRepository,
    private val bookstoreRepository: BookstoreRepository,
    private val introducedBookstoreRepository: IntroducedBookstoreRepository,
    private val articleTagRepository: ArticleTagRepository,
    private val bookmarkService: BookmarkService
) {
    @Transactional
    fun createArticle(
        currentAccount: Account,
        articleRequest: ArticleRequest
    ): ArticleIdResponse {
        currentAccount.role.checkAdminAndManager()
        duplicateArticle(articleRequest.title)

        val bookStoreList = articleRequest.bookstoreList
        val articleTags = articleRequest.articleTagList
        val article = Article(articleRequest)
        val saveArticle = articleRepository.save(article)

        addBookstoresToArticle(saveArticle, bookStoreList)
        addArticleTags(saveArticle, articleTags)

        return ArticleIdResponse(saveArticle.id)
    }

    @Transactional
    fun updateArticle(
        currentAccount: Account,
        articleId: Long,
        articleRequest: ArticleRequest
    ): ArticleIdResponse {
        currentAccount.role.checkAdminAndManager()

        val article = getArticle(articleId)
        article.updateArticleData(articleRequest)

        removeIntroducedBookstores(article)
        addBookstoresToArticle(article, articleRequest.bookstoreList)
        removeArticleTags(article)
        addArticleTags(article, articleRequest.articleTagList)

        return ArticleIdResponse(article.id)
    }

    fun getArticleList(
        currentAccount: Account,
        pageable: Pageable,
        cursorId: Long?
    ): ArticlePageResponse {
        var nextCursorId = cursorId ?: 0L

        if (cursorId != null && cursorId == 0L) {
            val firstArticle = articleRepository.findFirstByStatusOrderByCreatedAtDesc(Status.VISIBLE)
                ?: throw BookandException(ErrorCode.NOT_FOUND_ARTICLE)
            nextCursorId = firstArticle.id
        }
        val date: String? = if (cursorId == null) null else getArticle(nextCursorId).createdAt.toString()
        val articlePageResponse: Page<ArticleResponse> = articleRepository
            .findAllByStatus(Status.VISIBLE, pageable, nextCursorId, date)
            .map { article ->
                ArticleResponse(
                    id = article.id,
                    title = article.title,
                    subTitle = article.subTitle,
                    mainImage = article.mainImage,
                    category = article.category,
                    content = article.content,
                    writer = article.writer,
                    status = article.status,
                    view = article.viewCount,
                    isBookmark = bookmarkService.checkBookmark(
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

        val totalElements = articleRepository.countAllByStatus(Status.VISIBLE)
        return ArticlePageResponse.of(articlePageResponse, totalElements)
    }

    fun getWebArticleList(currentAccount: Account, pageable: Pageable): ArticleWebPageResponse {
        val articlePage = articleRepository.findAll(pageable)
            .map(::ArticleWebResponse)
        return ArticleWebPageResponse.of(articlePage)

    }

    fun searchArticleList(
        search: String?,
        category: String?,
        status: String?,
        pageable: Pageable
    ): ArticleWebPageResponse {
        val articlePage = articleRepository.findAllBySearch(search, category, status, pageable)
            .map(::ArticleWebResponse)
        return ArticleWebPageResponse.of(articlePage)
    }


    fun getArticleInfo(currentAccount: Account, id: Long): ArticleDetailResponse {
        val article = getArticle(id)

        val bookmarkTypeArticle = BookmarkType.ARTICLE.name
        val bookmarkTypeBookstore = BookmarkType.BOOKSTORE.name
        val accountId = currentAccount.id
        val articleId = article.id

        val bookmarkedArticle = bookmarkService.checkBookmark(accountId, articleId, bookmarkTypeArticle)
        val articleTagList = article.articleTagList.map { it.tag }
        val bookStoreList = article.introducedBookstoreList.map { introducedBookstore ->
            val bookstore = introducedBookstore.bookstore
            val bookstoreId = bookstore.id
            val bookmarkedBookstore = bookmarkService.checkBookmark(accountId, bookstoreId, bookmarkTypeBookstore)

            BookstoreSimpleResponse(
                id = bookstoreId,
                name = bookstore.name,
                introduction = bookstore.introduction,
                mainImage = bookstore.mainImage,
                themeList = bookstore.themeList.map { it.theme.toString() },
                isBookmark = bookmarkedBookstore
            )
        }

        val filter = ArticleFilter(
            deviceOS = article.deviceOSFilter,
            memberId = article.memberIdFilter
        )


        return ArticleDetailResponse(
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
            modifiedDate = article.modifiedAt.toString()
        )
    }


    private fun addBookstoresToArticle(article: Article, bookstoreList: List<Long>) {
        bookstoreList.map { getBookstore(it) }.toList().forEach { bookstore ->
            val introducedBookstore = IntroducedBookstore(IntroducedBookstoreRequest(article, bookstore))
            val savedIntroducedBookstore = introducedBookstoreRepository.save(introducedBookstore)
            article.updateIntroducedBookstore(savedIntroducedBookstore)
            bookstore.updateIntroducedBookstore(savedIntroducedBookstore)
        }
    }


    private fun addArticleTags(article: Article, tagList: List<String>) {
        tagList.map { ArticleTag(tag = it) }.forEach { articleTag ->
            val savedArticleTag = articleTagRepository.save(articleTag)
            article.updateArticleTag(savedArticleTag)
            savedArticleTag.updateArticle(article)
        }
    }


    @Transactional
    fun updateArticleStatus(articleId: Long): ArticleIdResponse {
        val article = getArticle(articleId)
        val status = article.status
        if (status == Status.VISIBLE) article.updateArticleStatus(Status.INVISIBLE)
        else article.updateArticleStatus(Status.VISIBLE)
        return ArticleIdResponse(article.id)
    }

    fun removeArticle(currentAccount: Account, articleId: Long) {
        currentAccount.role.checkAdminAndManager()
        softDeleteArticle(getArticle(articleId))
    }

    fun removeArticleList(currentAccount: Account, list: ArticleListRequest) {
        currentAccount.role.checkAdminAndManager()
        list.articleIdList.forEach { articleId ->
            softDeleteArticle(getArticle(articleId))
        }
    }

    fun softDeleteArticle(article: Article){
        removeIntroducedBookstores(article)
        removeArticleTags(article)
        article.softDelete()
    }

    fun duplicateArticle(title: String) {
        if (isDuplicateArticle(title)) {
            throw BookandException(ErrorCode.DUPLICATE_ARTICLE)
        }
    }

    fun getBookstore(bookstoreId: Long): Bookstore {
        return bookstoreRepository.findById(bookstoreId)
            .orElseThrow { BookandException(ErrorCode.NOT_FOUND_BOOKSTORE) }
    }

    fun getArticle(articleId: Long): Article {
        return articleRepository.findById(articleId)
            .orElseThrow { BookandException(ErrorCode.NOT_FOUND_ARTICLE) }
    }

    private fun removeIntroducedBookstores(article: Article) {
        article.introducedBookstoreList.toList().forEach { introducedBookstore ->
            introducedBookstoreRepository.delete(introducedBookstore)
            introducedBookstore.bookstore.removeIntroducedBookstore(introducedBookstore)
            article.removeIntroducedBookstore(introducedBookstore)
        }
    }

    private fun removeArticleTags(article: Article) {
        article.articleTagList.toList().forEach { articleTag ->
            articleTagRepository.delete(articleTag)
            article.removeArticleTag(articleTag)
        }
    }

    private fun isDuplicateArticle(title: String): Boolean {
        return articleRepository.existsByTitle(title)
    }
}