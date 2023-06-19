package kr.co.bookand.backend.bookmark.service

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.article.repository.KotlinArticleRepository
import kr.co.bookand.backend.bookmark.domain.*
import kr.co.bookand.backend.bookmark.domain.dto.*
import kr.co.bookand.backend.bookmark.repository.KotlinBookmarkRepository
import kr.co.bookand.backend.bookmark.repository.KotlinBookmarkedArticleRepository
import kr.co.bookand.backend.bookmark.repository.KotlinBookmarkedBookstoreRepository
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreRepository
import kr.co.bookand.backend.common.KotlinErrorCode
import kr.co.bookand.backend.common.KotlinPageResponse
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
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
    fun createBookmarkedArticle(
        currentAccount: KotlinAccount,
        articleId: Long
    ): KotlinMessageResponse {
        val myBookmark = getMyInitBookmark(currentAccount.id, KotlinBookmarkType.ARTICLE)
        val article = getArticle(articleId)
        return checkBookmarkedArticle(myBookmark, article, currentAccount.id)
    }

    @Transactional
    fun createBookmarkedBookstore(
        currentAccount: KotlinAccount,
        bookstoreId: Long
    ): KotlinMessageResponse {
        val myBookmark = getMyInitBookmark(currentAccount.id, KotlinBookmarkType.BOOKSTORE)
        val bookstore = getBookstore(bookstoreId)
        return checkBookmarkedBookstore(myBookmark, bookstore, currentAccount.id)
    }

    @Transactional
    fun createBookmarkFolder(
        currentAccount: KotlinAccount,
        bookmarkFolderRequest: KotlinBookmarkFolderRequest
    ): KotlinBookmarkIdResponse {
        val bookmark = KotlinBookmark(currentAccount, bookmarkFolderRequest)
        val saveBookmark = kotlinBookmarkRepository.save(bookmark)
        return KotlinBookmarkIdResponse(saveBookmark.id)
    }

    fun getBookmarkFolderList(
        currentAccount: KotlinAccount,
        bookmarkType: String
    ): KotlinBookmarkFolderListResponse {
        val bookmarkType = KotlinBookmarkType.valueOf(bookmarkType)
        val bookmarkFolderResponseList =
            kotlinBookmarkRepository.findAllByAccountAndBookmarkType(currentAccount, bookmarkType)
                .map { KotlinBookmarkFolderResponse(it) }
        return KotlinBookmarkFolderListResponse(bookmarkFolderResponseList)
    }

    fun getBookmarkFolder(
        currentAccount: KotlinAccount,
        bookmarkId: Long,
        pageable: Pageable?,
        cursorId: Long?
    ): KotlinBookmarkResponse {
        val bookmark = getMyBookmark(currentAccount.id, bookmarkId)
        return getBookmarkResponse(bookmark, pageable, cursorId)
    }

    fun getBookmarkResponse(
        bookmark: KotlinBookmark,
        pageable: Pageable?,
        cursorId: Long?
    ): KotlinBookmarkResponse {
        if (bookmark.bookmarkType == KotlinBookmarkType.BOOKSTORE) {
            val firstByBookmarkId = kotlinBookmarkedBookstoreRepository.findFirstByBookmarkId(bookmark.id)
            val nextCursorId =
                if (cursorId != null && cursorId == 0L && firstByBookmarkId != null) firstByBookmarkId.bookstore.id
                else cursorId
            val createdAt =
                if (cursorId != null && cursorId == 0L && firstByBookmarkId != null) firstByBookmarkId.bookstore.createdAt.toString()
                else (if (cursorId == null) null else nextCursorId?.let { getBookmarkedBookstore(it).bookmark.createdAt.toString() })
            val page = kotlinBookmarkedBookstoreRepository.findAllByBookmarkAndAndVisibilityTrue(bookmark, pageable, cursorId, createdAt)
                .map { KotlinBookmarkInfo(it.bookstore) }
            val totalElements = kotlinBookmarkedBookstoreRepository.countAllByBookmark(bookmark)
            val ofCursor = KotlinPageResponse.ofCursor(page, totalElements)

            return KotlinBookmarkResponse(bookmark = bookmark, bookmarkInfo = ofCursor)
        } else {
            val firstByBookmarkId = kotlinBookmarkedArticleRepository.findFirstByBookmarkId(bookmark.id)
            val nextCursorId =
                if (cursorId != null && cursorId == 0L && firstByBookmarkId != null) firstByBookmarkId.article.id
                else cursorId
            val createdAt =
                if (cursorId != null && cursorId == 0L && firstByBookmarkId != null) firstByBookmarkId.article.createdAt.toString()
                else (if (cursorId == null) null else nextCursorId?.let { getBookmarkedArticle(it).bookmark.createdAt.toString() })
            val page = kotlinBookmarkedArticleRepository.findAllByBookmarkAndAndVisibilityTrue(bookmark, pageable, cursorId, createdAt)
                .map { KotlinBookmarkInfo(it.article) }
            val totalElements = kotlinBookmarkedArticleRepository.countAllByBookmark(bookmark)
            val ofCursor = KotlinPageResponse.ofCursor(page, totalElements)

            return KotlinBookmarkResponse(bookmark = bookmark, bookmarkInfo = ofCursor)
        }
    }

    fun getBookmarkCollect(
        currentAccount: KotlinAccount,
        bookmarkType: String,
        pageable: Pageable?,
        cursorId: Long?
    ): KotlinBookmarkResponse {
        val bookmarkType = KotlinBookmarkType.valueOf(bookmarkType)
        val bookmark = kotlinBookmarkRepository.findByAccountAndFolderNameAndBookmarkType(
            currentAccount,
            INIT_BOOKMARK_FOLDER_NAME,
            bookmarkType
        ) ?: throw RuntimeException(KotlinErrorCode.NOT_FOUND_BOOKMARK.errorMessage)
        return getBookmarkResponse(bookmark, pageable, cursorId)
    }

    @Transactional
    fun updateBookmarkFolderName(
        currentAccount: KotlinAccount,
        bookmarkId: Long,
        bookmarkFolderNameRequest: KotlinBookmarkFolderNameRequest
    ): KotlinBookmarkIdResponse {
        val bookmark = getMyBookmark(currentAccount.id, bookmarkId)
        checkBookmarkFolderName(bookmark)
        bookmark.updateFolderName(bookmarkFolderNameRequest.folderName)
        return KotlinBookmarkIdResponse(bookmark.id)
    }

    @Transactional
    fun updateBookmarkFolder(
        currentAccount: KotlinAccount,
        bookmarkId: Long,
        request: KotlinBookmarkContentListRequest
    ): KotlinBookmarkIdResponse {

        val myInitBookmark = getMyInitBookmark(currentAccount.id, KotlinBookmarkType.valueOf(request.bookmarkType))
        val bookmark = getMyBookmark(currentAccount.id, bookmarkId)
        checkUpdateBookmarkRequest(bookmark, request)
        val bookmarkBookStoreList: MutableList<KotlinBookmarkedBookstore> = mutableListOf()
        val bookmarkArticleList: MutableList<KotlinBookmarkedArticle> = mutableListOf()

        request.contentIdList.toList().map { contentId ->
            when (bookmark.bookmarkType) {
                KotlinBookmarkType.BOOKSTORE -> {
                    checkBookmarkedBookstoreInInitBookmark(myInitBookmark.id, contentId)
                    existBookmarkedBookstore(bookmark.id, contentId)
                    addBookmarkedBookstore(contentId, bookmark, currentAccount.id)
                }

                else -> {
                    checkBookmarkedArticleInInitBookmark(myInitBookmark.id, contentId)
                    existBookmarkedArticle(bookmark.id, contentId)
                    addBookmarkedArticle(contentId, bookmark, currentAccount.id)
                }
            }
        }

        when (bookmark.bookmarkType) {
            KotlinBookmarkType.BOOKSTORE -> bookmark.updateBookmarkedBookStore(bookmarkBookStoreList)
            else -> bookmark.updateBookmarkedArticle(bookmarkArticleList)
        }

        return KotlinBookmarkIdResponse(bookmark.id)
    }


    @Transactional
    fun deleteBookmarkContent(
        currentAccount: KotlinAccount,
        bookmarkId: Long,
        request: KotlinBookmarkContentListRequest
    ) {
        val bookmark = getMyBookmark(currentAccount.id, bookmarkId)
        checkUpdateBookmarkRequest(bookmark, request)
        val bookmarkContentIdList = request.contentIdList.toList()
        when (bookmark.bookmarkType) {
            KotlinBookmarkType.BOOKSTORE -> {
                bookmarkContentIdList.map { contentId ->
                    checkBookmarkedBookstore(bookmark.id, contentId)
                    deleteBookmarkBookstore(bookmark.id, contentId)
                }
            }

            else -> {
                bookmarkContentIdList.map { contentId ->
                    checkBookmarkedArticle(bookmark.id, contentId)
                    deleteBookmarkArticle(bookmark.id, contentId)
                }
            }
        }
    }

    @Transactional
    fun deleteInitBookmarkContent(
        currentAccount: KotlinAccount,
        request: KotlinBookmarkContentListRequest
    ) {

        val initBookmark = getMyInitBookmark(currentAccount.id, KotlinBookmarkType.valueOf(request.bookmarkType))
        checkUpdateBookmarkRequest(initBookmark, request)
        val bookmarkContentIdList = request.contentIdList.toList()

        when (initBookmark.bookmarkType) {
            KotlinBookmarkType.BOOKSTORE -> {
                bookmarkContentIdList.map { contentId ->
                    checkBookmarkedBookstoreInInitBookmark(initBookmark.id, contentId)
                    deleteBookmarkBookstore(initBookmark.id, contentId)
                    kotlinBookmarkRepository.findAllByAccountId(currentAccount.id)
                        .map { bookmark -> deleteBookmarkBookstore(bookmark.id, contentId) }
                }
            }

            else -> {
                bookmarkContentIdList.map { contentId ->
                    checkBookmarkedArticleInInitBookmark(initBookmark.id, contentId)
                    deleteBookmarkArticle(initBookmark.id, contentId)
                     kotlinBookmarkRepository.findAllByAccountId(currentAccount.id)
                        .map { bookmark -> deleteBookmarkArticle(bookmark.id, contentId) }
                }
            }
        }
    }

    @Transactional
    fun deleteBookmarkFolder(
        currentAccount: KotlinAccount,
        bookmarkId: Long
    ) {
        val bookmark = getMyBookmark(currentAccount.id, bookmarkId)
        checkBookmarkFolderName(bookmark)
        bookmark.softDelete()
        bookmark.bookmarkedArticleList.map { bookmarkedArticle ->
            kotlinBookmarkedArticleRepository.delete(bookmarkedArticle)
        }
        bookmark.bookmarkedBookstoreList.map { bookmarkedBookstore ->
            kotlinBookmarkedBookstoreRepository.delete(bookmarkedBookstore)
        }
    }


    fun checkBookmark(accountId: Long, contentId: Long, bookmarkType: String): Boolean {
        val account = kotlinAccountService.getAccountById(accountId)
        val bookmarkTypeEnum = KotlinBookmarkType.valueOf(bookmarkType)

        return account.bookmarkList.any { bookmark ->
            bookmark.folderName == INIT_BOOKMARK_FOLDER_NAME && bookmark.bookmarkType == bookmarkTypeEnum &&
                    when (bookmarkTypeEnum) {
                        KotlinBookmarkType.BOOKSTORE -> bookmark.bookmarkedBookstoreList.any { it.bookstore.id == contentId }
                        else -> bookmark.bookmarkedArticleList.any { it.article.id == contentId }
                    }
        }
    }


    fun addBookmarkedBookstore(contentId: Long, bookmark: KotlinBookmark, accountId: Long): KotlinBookmarkedBookstore {
        val account = kotlinAccountService.getAccountById(accountId)
        val bookstore = getBookstore(contentId)
        val kotlinBookmarkedBookstore =
            KotlinBookmarkedBookstore(bookmark = bookmark, bookstore = bookstore, account = account)
        val saveBookmarkedBookstore = kotlinBookmarkedBookstoreRepository.save(kotlinBookmarkedBookstore)
        bookmark.updateFolderImage(bookstore.mainImage)
        return saveBookmarkedBookstore
    }

    fun addBookmarkedArticle(contentId: Long, bookmark: KotlinBookmark, accountId: Long): KotlinBookmarkedArticle {
        val account = kotlinAccountService.getAccountById(accountId)
        val article = getArticle(contentId)
        val kotlinBookmarkedArticle = KotlinBookmarkedArticle(bookmark = bookmark, article = article, account = account)
        val saveBookmarkedArticle = kotlinBookmarkedArticleRepository.save(kotlinBookmarkedArticle)
        bookmark.updateFolderImage(article.mainImage)
        return saveBookmarkedArticle
    }

    fun getMyInitBookmark(accountId: Long, bookmarkType: KotlinBookmarkType): KotlinBookmark {
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

    fun getBookmarkedArticle(bookmarkedArticleId: Long): KotlinBookmarkedArticle {
        return kotlinBookmarkedArticleRepository.findById(bookmarkedArticleId)
            .orElseThrow { RuntimeException("NOT FOUND INIT BOOKMARK ARTICLE") }
    }

    fun getArticle(articleId: Long): KotlinArticle {
        return kotlinArticleRepository.findById(articleId)
            .orElseThrow { throw RuntimeException("NOT FOUND ARTICLE") }
    }

    fun getBookstore(bookstoreId: Long): KotlinBookstore {
        return kotlinBookstoreRepository.findById(bookstoreId)
            .orElseThrow { throw RuntimeException("NOT FOUND BOOKSTORE") }
    }

    fun checkBookmarkedArticle(
        myBookmark: KotlinBookmark,
        article: KotlinArticle,
        accountId: Long
    ): KotlinMessageResponse {
        val isBookmarkedArticle = kotlinBookmarkedArticleRepository
            .existsByBookmarkIdAndArticleIdAndAccountId(myBookmark.id, article.id, accountId)

        return if (isBookmarkedArticle) {
            deleteBookmarkArticle(myBookmark.id, article.id)
            KotlinMessageResponse(message = "북마크 삭제", statusCode = 200)
        } else {
            createBookmarkArticle(myBookmark, article, accountId)
            KotlinMessageResponse(message = "북마크 추가", statusCode = 200)
        }
    }

    fun checkBookmarkedBookstore(
        myBookmark: KotlinBookmark,
        bookstore: KotlinBookstore,
        accountId: Long
    ): KotlinMessageResponse {
        val isBookmarkedBookstore = kotlinBookmarkedBookstoreRepository
            .existsByBookmarkIdAndBookstoreIdAndAccountId(myBookmark.id, bookstore.id, accountId)

        return if (isBookmarkedBookstore) {
            deleteBookmarkBookstore(myBookmark.id, bookstore.id)
            KotlinMessageResponse(message = "북마크 삭제", statusCode = 200)
        } else {
            createBookmarkBookstore(myBookmark, bookstore, accountId)
            KotlinMessageResponse(message = "북마크 추가", statusCode = 200)
        }
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
        val account = kotlinAccountService.getAccountById(accountId)
        val bookmarkArticle = KotlinBookmarkedArticle(
            bookmark = myBookmark,
            article = article,
            account = account
        )
        myBookmark.addBookmarkedArticle(bookmarkArticle)
    }

    fun createBookmarkBookstore(myBookmark: KotlinBookmark, bookstore: KotlinBookstore, accountId: Long) {
        val account = kotlinAccountService.getAccountById(accountId)
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