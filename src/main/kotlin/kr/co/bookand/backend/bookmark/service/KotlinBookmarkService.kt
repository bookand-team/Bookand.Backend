package kr.co.bookand.backend.bookmark.service

import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.article.repository.KotlinArticleRepository
import kr.co.bookand.backend.bookmark.domain.*
import kr.co.bookand.backend.bookmark.domain.dto.KotlinBookmarkContentListRequest
import kr.co.bookand.backend.bookmark.domain.dto.KotlinBookmarkFolderRequest
import kr.co.bookand.backend.bookmark.domain.dto.KotlinBookmarkIdResponse
import kr.co.bookand.backend.bookmark.repository.KotlinBookmarkRepository
import kr.co.bookand.backend.bookmark.repository.KotlinBookmarkedArticleRepository
import kr.co.bookand.backend.bookmark.repository.KotlinBookmarkedBookstoreRepository
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

class KotlinBookmarkService(
    private val kotlinBookmarkRepository: KotlinBookmarkRepository,
    private val kotlinBookmarkedArticleRepository: KotlinBookmarkedArticleRepository,
    private val kotlinBookmarkedBookstoreRepository: KotlinBookmarkedBookstoreRepository,
    private val kotlinAccountService: KotlinAccountService,
    private val kotlinBookstoreRepository: KotlinBookstoreRepository,
    private val kotlinArticleRepository: KotlinArticleRepository
) {
    @Transactional
    fun createBookmarkedArticle(accountId: Long, articleId: Long) {
        val myBookmark = getMyInitBookmark(accountId, BookmarkType.ARTICLE)
        val article = getArticle(articleId)
        checkBookmarkedArticle(myBookmark, article, accountId)
    }

    @Transactional
    fun createBookmarkedBookstore(accountId: Long, bookstoreId: Long) {
        val myBookmark = getMyInitBookmark(accountId, BookmarkType.BOOKSTORE)
        val bookstore = getBookstore(bookstoreId)
        checkBookmarkedBookstore(myBookmark, bookstore, accountId)
    }

    @Transactional
    fun createBookmarkFolder(
        accountId: Long,
        bookmarkFolderRequest: KotlinBookmarkFolderRequest
    ): KotlinBookmarkIdResponse {
        val account = kotlinAccountService.getAccount(accountId)
        val bookmark = KotlinBookmark(account, bookmarkFolderRequest)
        val saveBookmark = kotlinBookmarkRepository.save(bookmark)
        return KotlinBookmarkIdResponse(saveBookmark.id)
    }

    @Transactional
    fun updateBookmarkFolderName(
        accountId: Long,
        bookmarkId: Long,
        bookmarkFolderRequest: KotlinBookmarkFolderRequest
    ): KotlinBookmarkIdResponse {
        val bookmark = getMyBookmark(accountId, bookmarkId)
        checkBookmarkFolderName(bookmark)
        bookmark.updateFolderName(bookmarkFolderRequest.folderName)
        return KotlinBookmarkIdResponse(bookmark.id)
    }

    @Transactional
    fun updateBookmarkFolder(
        accountId: Long,
        bookmarkId: Long,
        request: KotlinBookmarkContentListRequest
    ): KotlinBookmarkIdResponse {

        val myInitBookmark = getMyInitBookmark(accountId, BookmarkType.valueOf(request.bookmarkType))
        val bookmark = getMyBookmark(accountId, bookmarkId)
        checkUpdateBookmarkRequest(bookmark, request)
        val bookmarkBookStoreList: MutableList<KotlinBookmarkedBookstore> = mutableListOf()
        val bookmarkArticleList: MutableList<KotlinBookmarkedArticle> = mutableListOf()

        request.contentIdList.toList().map { contentId ->
            when (bookmark.bookmarkType) {
                BookmarkType.BOOKSTORE -> {
                    checkBookmarkedBookstoreInInitBookmark(myInitBookmark.id, contentId)
                    existBookmarkedBookstore(bookmark.id, contentId)
                    addBookmarkedBookstore(contentId, bookmark, accountId)
                }

                else -> {
                    checkBookmarkedArticleInInitBookmark(myInitBookmark.id, contentId)
                    existBookmarkedArticle(bookmark.id, contentId)
                    addBookmarkedArticle(contentId, bookmark, accountId)
                }
            }
        }

        when (bookmark.bookmarkType) {
            BookmarkType.BOOKSTORE -> bookmark.updateBookmarkedBookStore(bookmarkBookStoreList)
            else -> bookmark.updateBookmarkedArticle(bookmarkArticleList)
        }

        return KotlinBookmarkIdResponse(bookmark.id)
    }


    @Transactional
    fun deleteBookmarkContent(
        accountId: Long,
        bookmarkId: Long,
        request: KotlinBookmarkContentListRequest
    ) {
        val bookmark = getMyBookmark(accountId, bookmarkId)
        checkUpdateBookmarkRequest(bookmark, request)
        val bookmarkContentIdList = request.contentIdList.toList()
        when (bookmark.bookmarkType) {
            BookmarkType.BOOKSTORE -> {
                bookmarkContentIdList.map { contentId ->
                    checkBookmarkedBookstore(bookmark.id, contentId)
                    getBookmarkedBookstore(bookmarkId, contentId)
                    kotlinBookmarkedBookstoreRepository.deleteByBookstoreIdAndBookmarkId(contentId, bookmarkId)
                }
            }

            else -> {
                bookmarkContentIdList.map { contentId ->
                    checkBookmarkedArticle(bookmark.id, contentId)
                    getBookmarkedArticle(bookmarkId, contentId)
                    kotlinBookmarkedArticleRepository.deleteByArticleIdAndBookmarkId(contentId, bookmarkId)
                }
            }
        }
    }

    @Transactional
    fun deleteBookmarkFolder(accountId: Long, bookmarkId: Long) {
        val bookmark = getMyBookmark(accountId, bookmarkId)
        checkBookmarkFolderName(bookmark)
        bookmark.softDelete()
        bookmark.bookmarkedArticleList.map { bookmarkedArticle ->
            kotlinBookmarkedArticleRepository.delete(bookmarkedArticle)
        }
        bookmark.bookmarkedBookstoreList.map { bookmarkedBookstore ->
            kotlinBookmarkedBookstoreRepository.delete(bookmarkedBookstore)
        }
    }

    @Transactional
    fun deleteInitBookmarkContent(
        accountId: Long,
        request: KotlinBookmarkContentListRequest
    ) {
        val initBookmark = getMyInitBookmark(accountId, BookmarkType.valueOf(request.bookmarkType))

        // 모아보기에서 내용 삭제 시 다른 북마크에서도 삭제
        val bookmarkContentIdList = request.contentIdList.toList()
        when (initBookmark.bookmarkType) {
            BookmarkType.BOOKSTORE -> {
                bookmarkContentIdList.map { contentId ->
                    checkBookmarkedBookstoreInInitBookmark(initBookmark.id, contentId)
                    kotlinBookmarkedBookstoreRepository.deleteByBookstoreIdAndBookmarkId(contentId, initBookmark.id)
                    val bookmarkList = kotlinBookmarkRepository.findAllByAccountId(accountId)
                    bookmarkList.map { bookmark ->
                        kotlinBookmarkedBookstoreRepository.deleteByBookstoreIdAndBookmarkId(contentId, bookmark.id)
                    }
                }
            }

            else -> {
                bookmarkContentIdList.map { contentId ->
                    checkBookmarkedArticleInInitBookmark(initBookmark.id, contentId)
                    kotlinBookmarkedArticleRepository.deleteByArticleIdAndBookmarkId(contentId, initBookmark.id)
                    val bookmarkList = kotlinBookmarkRepository.findAllByAccountId(accountId)
                    bookmarkList.map { bookmark ->
                        kotlinBookmarkedArticleRepository.deleteByArticleIdAndBookmarkId(contentId, bookmark.id)
                    }
                }
            }
        }

    }

    fun checkBookmark(accountId: Long, contentId: Long, bookmarkType: String): Boolean {
        val account = kotlinAccountService.getAccount(accountId)
        val bookmarkTypeEnum = BookmarkType.valueOf(bookmarkType)

        return account.bookmarkList.any { bookmark ->
            bookmark.folderName == INIT_BOOKMARK_FOLDER_NAME && bookmark.bookmarkType == bookmarkTypeEnum &&
                    when (bookmarkTypeEnum) {
                        BookmarkType.BOOKSTORE -> bookmark.bookmarkedBookstoreList.any { it.bookstore.id == contentId }
                        else -> bookmark.bookmarkedArticleList.any { it.article.id == contentId }
                    }
        }
    }


    fun addBookmarkedBookstore(contentId: Long, bookmark: KotlinBookmark, accountId: Long): KotlinBookmarkedBookstore {
        val account = kotlinAccountService.getAccount(accountId)
        val bookstore = getBookstore(contentId)
        val kotlinBookmarkedBookstore =
            KotlinBookmarkedBookstore(bookmark = bookmark, bookstore = bookstore, account = account)
        val saveBookmarkedBookstore = kotlinBookmarkedBookstoreRepository.save(kotlinBookmarkedBookstore)
        bookmark.updateFolderImage(bookstore.mainImage)
        return saveBookmarkedBookstore
    }

    fun addBookmarkedArticle(contentId: Long, bookmark: KotlinBookmark, accountId: Long): KotlinBookmarkedArticle {
        val account = kotlinAccountService.getAccount(accountId)
        val article = getArticle(contentId)
        val kotlinBookmarkedArticle = KotlinBookmarkedArticle(bookmark = bookmark, article = article, account = account)
        val saveBookmarkedArticle = kotlinBookmarkedArticleRepository.save(kotlinBookmarkedArticle)
        bookmark.updateFolderImage(article.mainImage)
        return saveBookmarkedArticle
    }

    fun getMyInitBookmark(accountId: Long, bookmarkType: BookmarkType): KotlinBookmark {
        return kotlinBookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(
            accountId,
            INIT_BOOKMARK_FOLDER_NAME,
            bookmarkType
        ) ?: throw RuntimeException("NOT FOUND INIT BOOKMARK")
    }

    fun getMyBookmark(accountId: Long, bookmarkId: Long): KotlinBookmark {
        return kotlinBookmarkRepository.findByIdAndAccountId(bookmarkId, accountId)
            ?: throw RuntimeException("NOT FOUND INIT BOOKMARK")
    }

    fun getBookmarkedBookstore(bookmarkedBookstoreId: Long): KotlinBookmarkedBookstore {
        return kotlinBookmarkedBookstoreRepository.findById(bookmarkedBookstoreId)
            .orElseThrow { throw RuntimeException("NOT FOUND INIT BOOKMARK BOOKSTORE") }
    }

    fun getBookmarkedBookstore(bookmarkId: Long, bookstoreId: Long): KotlinBookmarkedBookstore {
        return kotlinBookmarkedBookstoreRepository.findByBookmarkIdAndBookstoreId(bookmarkId, bookstoreId)
            ?: throw RuntimeException("NOT FOUND INIT BOOKMARK BOOKSTORE")
    }

    fun getBookmarkedArticle(bookmarkedArticleId: Long): KotlinBookmarkedArticle {
        return kotlinBookmarkedArticleRepository.findById(bookmarkedArticleId)
            .orElseThrow { RuntimeException("NOT FOUND INIT BOOKMARK ARTICLE") }
    }

    fun getBookmarkedArticle(bookmarkId: Long, articleId: Long): KotlinBookmarkedArticle {
        return kotlinBookmarkedArticleRepository.findByBookmarkIdAndArticleId(bookmarkId, articleId)
            ?: throw RuntimeException("NOT FOUND INIT BOOKMARK ARTICLE")
    }

    fun getArticle(articleId: Long): KotlinArticle {
        return kotlinArticleRepository.findById(articleId)
            .orElseThrow { throw RuntimeException("NOT FOUND ARTICLE") }
    }

    fun getBookstore(bookstoreId: Long): KotlinBookstore {
        return kotlinBookstoreRepository.findById(bookstoreId)
            .orElseThrow { throw RuntimeException("NOT FOUND BOOKSTORE") }
    }

    fun checkBookmarkedArticle(myBookmark: KotlinBookmark, article: KotlinArticle, accountId: Long) {
        val isBookmarkedArticle = kotlinBookmarkedArticleRepository
            .existsByBookmarkIdAndArticleIdAndAccountId(myBookmark.id, article.id, accountId)

        if (isBookmarkedArticle) deleteBookmarkArticle(myBookmark.id, article.id)
        else createBookmarkArticle(myBookmark, article, accountId)
    }

    fun checkBookmarkedBookstore(myBookmark: KotlinBookmark, bookstore: KotlinBookstore, accountId: Long) {
        val isBookmarkedBookstore = kotlinBookmarkedBookstoreRepository
            .existsByBookmarkIdAndBookstoreIdAndAccountId(myBookmark.id, bookstore.id, accountId)

        if (isBookmarkedBookstore) deleteBookmarkBookstore(myBookmark.id, bookstore.id)
        else createBookmarkBookstore(myBookmark, bookstore, accountId)
    }

    fun checkUpdateBookmarkRequest(bookmark: KotlinBookmark, request: KotlinBookmarkContentListRequest) {
        if (bookmark.bookmarkType.toString() != request.bookmarkType) throw RuntimeException("NOT MATCH BOOKMARK TYPE")
    }

    fun checkBookmarkedBookstoreInInitBookmark(myInitBookmarkId: Long, contentId: Long) {
        kotlinBookmarkedBookstoreRepository.findByBookmarkIdAndBookstoreId(myInitBookmarkId, contentId)
            ?: throw RuntimeException("NOT FOUND BOOKMARKED BOOKSTORE")
    }

    fun checkBookmarkedArticleInInitBookmark(myInitBookmarkId: Long, contentId: Long) {
        kotlinBookmarkedArticleRepository.findByBookmarkIdAndArticleId(myInitBookmarkId, contentId)
            ?: throw RuntimeException("NOT FOUND BOOKMARKED ARTICLE")
    }

    fun existBookmarkedBookstore(bookmarkId: Long, contentId: Long) {
        val checkExist =
            kotlinBookmarkedBookstoreRepository.existsByBookmarkIdAndBookstoreId(bookmarkId, contentId)
        if (checkExist) throw RuntimeException("ALREADY EXIST BOOKMARKED BOOKSTORE")
    }

    fun checkBookmarkedBookstore(bookmarkId: Long, contentId: Long) {
        kotlinBookmarkedBookstoreRepository.findByBookmarkIdAndBookstoreId(bookmarkId, contentId)
            ?: throw RuntimeException("NOT FOUND BOOKMARKED BOOKSTORE")
    }

    fun existBookmarkedArticle(bookmarkId: Long, contentId: Long) {
        val checkExist =
            kotlinBookmarkedArticleRepository.existsByBookmarkIdAndArticleId(bookmarkId, contentId)
        if (checkExist) throw RuntimeException("ALREADY EXIST BOOKMARKED ARTICLE")
    }

    fun checkBookmarkedArticle(bookmarkId: Long, contentId: Long) {
        kotlinBookmarkedArticleRepository.findByBookmarkIdAndArticleId(bookmarkId, contentId)
            ?: throw RuntimeException("NOT FOUND BOOKMARKED ARTICLE")
    }

    fun checkBookmarkFolderName(bookmark: KotlinBookmark) {
        if (bookmark.folderName == INIT_BOOKMARK_FOLDER_NAME) {
            throw RuntimeException("NOT CHANGE INIT BOOKMARK")
        }
    }

    fun deleteBookmarkArticle(myBookmarkId: Long, articleId: Long) {
        kotlinBookmarkedArticleRepository.deleteByArticleIdAndBookmarkId(articleId, myBookmarkId)
    }

    fun deleteBookmarkBookstore(myBookmarkId: Long, bookstoreId: Long) {
        kotlinBookmarkedBookstoreRepository.deleteByBookstoreIdAndBookmarkId(bookstoreId, myBookmarkId)
    }

    fun createBookmarkArticle(myBookmark: KotlinBookmark, article: KotlinArticle, accountId: Long) {
        val account = kotlinAccountService.getAccount(accountId)
        val bookmarkArticle = KotlinBookmarkedArticle(
            bookmark = myBookmark,
            article = article,
            account = account
        )
        myBookmark.addBookmarkedArticle(bookmarkArticle)
    }

    fun createBookmarkBookstore(myBookmark: KotlinBookmark, bookstore: KotlinBookstore, accountId: Long) {
        val account = kotlinAccountService.getAccount(accountId)
        val bookmarkBookstore = KotlinBookmarkedBookstore(
            bookmark = myBookmark,
            bookstore = bookstore,
            account = account
        )
        myBookmark.addBookmarkedBookstore(bookmarkBookstore)
    }

    private companion object {
        const val INIT_BOOKMARK_FOLDER_NAME = "모아보기"
    }

}