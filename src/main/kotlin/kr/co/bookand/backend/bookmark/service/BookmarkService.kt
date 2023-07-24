package kr.co.bookand.backend.bookmark.service

import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.article.model.Article
import kr.co.bookand.backend.article.repository.ArticleRepository
import kr.co.bookand.backend.bookmark.dto.*
import kr.co.bookand.backend.bookmark.model.*
import kr.co.bookand.backend.bookmark.repository.BookmarkRepository
import kr.co.bookand.backend.bookmark.repository.BookmarkedArticleRepository
import kr.co.bookand.backend.bookmark.repository.BookmarkedBookstoreRepository
import kr.co.bookand.backend.bookstore.model.Bookstore
import kr.co.bookand.backend.bookstore.repository.BookstoreRepository
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.common.model.MessageResponse
import kr.co.bookand.backend.common.exception.BookandException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BookmarkService(
    private val bookmarkRepository: BookmarkRepository,
    private val bookmarkedArticleRepository: BookmarkedArticleRepository,
    private val bookmarkedBookstoreRepository: BookmarkedBookstoreRepository,
    private val accountService: AccountService,
    private val bookstoreRepository: BookstoreRepository,
    private val articleRepository: ArticleRepository
) {
    @Transactional
    fun createBookmarkedArticle(
        currentAccount: Account,
        articleId: Long
    ): MessageResponse {
        val myBookmark = getMyInitBookmark(currentAccount.id, BookmarkType.ARTICLE)
        val article = getArticle(articleId)
        return checkBookmarkedArticle(myBookmark, article, currentAccount.id)
    }

    @Transactional
    fun createBookmarkedBookstore(
        currentAccount: Account,
        bookstoreId: Long
    ): MessageResponse {
        val myBookmark = getMyInitBookmark(currentAccount.id, BookmarkType.BOOKSTORE)
        val bookstore = getBookstore(bookstoreId)
        return checkBookmarkedBookstore(myBookmark, bookstore, currentAccount.id)
    }

    @Transactional
    fun createBookmarkFolder(
        currentAccount: Account,
        bookmarkFolderRequest: BookmarkFolderRequest
    ): BookmarkIdResponse {
        val bookmark = Bookmark(currentAccount, bookmarkFolderRequest)
        val saveBookmark = bookmarkRepository.save(bookmark)
        return BookmarkIdResponse(saveBookmark.id)
    }

    fun getBookmarkFolderList(
        currentAccount: Account,
        bookmarkType: String
    ): BookmarkFolderListResponse {
        val bookmarkType = BookmarkType.valueOf(bookmarkType)
        val bookmarkFolderResponseList =
            bookmarkRepository.findAllByAccountAndBookmarkTypeAndVisibilityTrue(currentAccount, bookmarkType)
                .filter { it.folderName != "모아보기" }
                .map { BookmarkFolderResponse(it) }
        return BookmarkFolderListResponse(bookmarkFolderResponseList)
    }

    fun getBookmarkFolder(
        currentAccount: Account,
        bookmarkId: Long,
        pageable: Pageable,
        cursorId: Long?
    ): BookmarkResponse {
        val bookmark = getMyBookmark(currentAccount.id, bookmarkId)
        return getBookmarkResponse(bookmark, pageable, cursorId)
    }

    fun getBookmarkResponse(
        bookmark: Bookmark,
        pageable: Pageable,
        cursorId: Long?
    ): BookmarkResponse {
        if (bookmark.bookmarkType == BookmarkType.BOOKSTORE) {
            val firstByBookmarkId = bookmarkedBookstoreRepository.findFirstByBookmarkId(bookmark.id)
            val nextCursorId =
                if (cursorId != null && cursorId != 0L && firstByBookmarkId != null) firstByBookmarkId.bookstore.id
                else cursorId
            val createdAt =
                if (cursorId != null && cursorId != 0L && firstByBookmarkId != null) firstByBookmarkId.bookstore.createdAt.toString()
                else null
            val page = bookmarkedBookstoreRepository.findAllByBookmarkAndAndVisibilityTrue(bookmark, pageable, nextCursorId, createdAt)
                .map { BookmarkInfo(it.bookstore) }
            val totalElements = bookmarkedBookstoreRepository.countAllByBookmark(bookmark)
            val ofCursor = PageResponse.ofCursor(page, totalElements)

            return BookmarkResponse(bookmark = bookmark, bookmarkInfo = ofCursor)
        } else {
            val firstByBookmarkId = bookmarkedArticleRepository.findFirstByBookmarkId(bookmark.id)
            val nextCursorId =
                if (cursorId != null && cursorId != 0L && firstByBookmarkId != null) firstByBookmarkId.article.id
                else cursorId
            val createdAt =
                if (cursorId != null && cursorId != 0L && firstByBookmarkId != null) firstByBookmarkId.article.createdAt.toString()
                else null
            val page = bookmarkedArticleRepository.findAllByBookmarkAndAndVisibilityTrue(bookmark, pageable, nextCursorId, createdAt)
                .map { BookmarkInfo(it.article) }
            val totalElements = bookmarkedArticleRepository.countAllByBookmark(bookmark)
            val ofCursor = PageResponse.ofCursor(page, totalElements)

            return BookmarkResponse(bookmark = bookmark, bookmarkInfo = ofCursor)
        }
    }

    fun getBookmarkCollect(
        currentAccount: Account,
        bookmarkType: String,
        pageable: Pageable,
        cursorId: Long?
    ): BookmarkResponse {
        val bookmarkType = BookmarkType.valueOf(bookmarkType)
        val bookmark = bookmarkRepository.findByAccountAndFolderNameAndBookmarkTypeAndVisibilityTrue(
            currentAccount,
            INIT_BOOKMARK_FOLDER_NAME,
            bookmarkType
        ) ?: throw BookandException(ErrorCode.NOT_FOUND_BOOKMARK, )
        return getBookmarkResponse(bookmark, pageable, cursorId)
    }

    @Transactional
    fun updateBookmarkFolderName(
        currentAccount: Account,
        bookmarkId: Long,
        bookmarkFolderNameRequest: BookmarkFolderNameRequest
    ): BookmarkIdResponse {
        val bookmark = getMyBookmark(currentAccount.id, bookmarkId)
        checkBookmarkFolderName(bookmark)
        bookmark.updateFolderName(bookmarkFolderNameRequest.folderName)
        return BookmarkIdResponse(bookmark.id)
    }

    @Transactional
    fun updateBookmarkFolder(
        currentAccount: Account,
        bookmarkId: Long,
        request: BookmarkContentListRequest
    ): BookmarkIdResponse {

        val myInitBookmark = getMyInitBookmark(currentAccount.id, BookmarkType.valueOf(request.bookmarkType))
        val bookmark = getMyBookmark(currentAccount.id, bookmarkId)
        checkUpdateBookmarkRequest(bookmark, request)
        val bookmarkBookStoreList: MutableList<BookmarkedBookstore> = mutableListOf()
        val bookmarkArticleList: MutableList<BookmarkedArticle> = mutableListOf()

        request.contentIdList.toList().map { contentId ->
            when (bookmark.bookmarkType) {
                BookmarkType.BOOKSTORE -> {
                    checkBookmarkedBookstoreInInitBookmark(myInitBookmark.id, contentId)
                    existBookmarkedBookstore(bookmark.id, contentId)
                    bookmarkBookStoreList.add(addBookmarkedBookstore(contentId, bookmark, currentAccount.id))
                }

                else -> {
                    checkBookmarkedArticleInInitBookmark(myInitBookmark.id, contentId)
                    existBookmarkedArticle(bookmark.id, contentId)
                    bookmarkArticleList.add(addBookmarkedArticle(contentId, bookmark, currentAccount.id))
                }
            }
        }
        return BookmarkIdResponse(bookmark.id)
    }


    @Transactional
    fun deleteBookmarkContent(
        currentAccount: Account,
        bookmarkId: Long,
        request: BookmarkContentListRequest
    ) {
        val bookmark = getMyBookmark(currentAccount.id, bookmarkId)
        checkUpdateBookmarkRequest(bookmark, request)
        val bookmarkContentIdList = request.contentIdList.toList()
        when (bookmark.bookmarkType) {
            BookmarkType.BOOKSTORE -> {
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
        currentAccount: Account,
        request: BookmarkContentListRequest
    ) {

        val initBookmark = getMyInitBookmark(currentAccount.id, BookmarkType.valueOf(request.bookmarkType))
        checkUpdateBookmarkRequest(initBookmark, request)
        val bookmarkContentIdList = request.contentIdList.toList()

        when (initBookmark.bookmarkType) {
            BookmarkType.BOOKSTORE -> {
                bookmarkContentIdList.map { contentId ->
                    checkBookmarkedBookstoreInInitBookmark(initBookmark.id, contentId)
                    deleteBookmarkBookstore(initBookmark.id, contentId)
                    bookmarkRepository.findAllByAccountId(currentAccount.id)
                        .map { bookmark -> deleteBookmarkBookstore(bookmark.id, contentId) }
                }
            }

            else -> {
                bookmarkContentIdList.map { contentId ->
                    checkBookmarkedArticleInInitBookmark(initBookmark.id, contentId)
                    deleteBookmarkArticle(initBookmark.id, contentId)
                     bookmarkRepository.findAllByAccountId(currentAccount.id)
                        .map { bookmark -> deleteBookmarkArticle(bookmark.id, contentId) }
                }
            }
        }
    }

    @Transactional
    fun deleteBookmarkFolder(
        currentAccount: Account,
        bookmarkId: Long
    ) {
        val bookmark = getMyBookmark(currentAccount.id, bookmarkId)
        checkBookmarkFolderName(bookmark)
        bookmark.softDelete()
        bookmark.bookmarkedArticleList.map { bookmarkedArticle ->
            bookmarkedArticle.softDelete()
        }
        bookmark.bookmarkedBookstoreList.map { bookmarkedBookstore ->
            bookmarkedBookstore.softDelete()
        }
    }


    fun checkBookmark(accountId: Long, contentId: Long, bookmarkType: String): Boolean {
        val account = accountService.getAccountById(accountId)
        val bookmarkTypeEnum = BookmarkType.valueOf(bookmarkType)

        return account.bookmarkList.any { bookmark ->
            bookmark.folderName == INIT_BOOKMARK_FOLDER_NAME && bookmark.bookmarkType == bookmarkTypeEnum &&
                    when (bookmarkTypeEnum) {
                        BookmarkType.BOOKSTORE -> bookmark.bookmarkedBookstoreList.any { it.bookstore.id == contentId }
                        else -> bookmark.bookmarkedArticleList.any { it.article.id == contentId }
                    }
        }
    }


    fun addBookmarkedBookstore(contentId: Long, bookmark: Bookmark, accountId: Long): BookmarkedBookstore {
        val account = accountService.getAccountById(accountId)
        val bookstore = getBookstore(contentId)
        val bookmarkedBookstore =
            BookmarkedBookstore(bookmark = bookmark, bookstore = bookstore, account = account)
        val saveBookmarkedBookstore = bookmarkedBookstoreRepository.save(bookmarkedBookstore)
        bookmark.updateFolderImage(bookstore.mainImage)
        return saveBookmarkedBookstore
    }

    fun addBookmarkedArticle(contentId: Long, bookmark: Bookmark, accountId: Long): BookmarkedArticle {
        val account = accountService.getAccountById(accountId)
        val article = getArticle(contentId)
        val bookmarkedArticle = BookmarkedArticle(bookmark = bookmark, article = article, account = account)
        val saveBookmarkedArticle = bookmarkedArticleRepository.save(bookmarkedArticle)
        bookmark.updateFolderImage(article.mainImage)
        return saveBookmarkedArticle
    }

    fun getMyInitBookmark(accountId: Long, bookmarkType: BookmarkType): Bookmark {
        return bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkTypeAndVisibilityTrue(
            accountId,
            INIT_BOOKMARK_FOLDER_NAME,
            bookmarkType
        ) ?: throw BookandException(ErrorCode.NOT_FOUND_INIT_BOOKMARK)
    }

    fun getMyBookmark(accountId: Long, bookmarkId: Long): Bookmark {
        return bookmarkRepository.findByIdAndAccountIdAndVisibilityTrue(bookmarkId, accountId)
            ?: throw BookandException(ErrorCode.NOT_FOUND_BOOKMARK)
    }

    fun getArticle(articleId: Long): Article {
        return articleRepository.findById(articleId)
            .orElseThrow { throw BookandException(ErrorCode.NOT_FOUND_ARTICLE) }
    }

    fun getBookstore(bookstoreId: Long): Bookstore {
        return bookstoreRepository.findById(bookstoreId)
            .orElseThrow { throw BookandException(ErrorCode.NOT_FOUND_BOOKSTORE) }
    }

    fun checkBookmarkedArticle(
        myBookmark: Bookmark,
        article: Article,
        accountId: Long
    ): MessageResponse {
        val isBookmarkedArticle = bookmarkedArticleRepository
            .existsByBookmarkIdAndArticleIdAndAccountId(myBookmark.id, article.id, accountId)

        return if (isBookmarkedArticle) {
            deleteBookmarkArticle(myBookmark.id, article.id)
            MessageResponse(result = "북마크 삭제", statusCode = 200)
        } else {
            createBookmarkArticle(myBookmark, article, accountId)
            MessageResponse(result = "북마크 추가", statusCode = 200)
        }
    }

    fun checkBookmarkedBookstore(
        myBookmark: Bookmark,
        bookstore: Bookstore,
        accountId: Long
    ): MessageResponse {
        val isBookmarkedBookstore = bookmarkedBookstoreRepository
            .existsByBookmarkIdAndBookstoreIdAndAccountId(myBookmark.id, bookstore.id, accountId)

        return if (isBookmarkedBookstore) {
            deleteBookmarkBookstore(myBookmark.id, bookstore.id)
            MessageResponse(result = "북마크 삭제", statusCode = 200)
        } else {
            createBookmarkBookstore(myBookmark, bookstore, accountId)
            MessageResponse(result = "북마크 추가", statusCode = 200)
        }
    }

    fun checkUpdateBookmarkRequest(bookmark: Bookmark, request: BookmarkContentListRequest) {
        if (bookmark.bookmarkType.toString() != request.bookmarkType) throw BookandException(ErrorCode.NOT_MATCH_BOOKMARK_TYPE)
    }

    fun checkBookmarkedBookstoreInInitBookmark(myInitBookmarkId: Long, contentId: Long) {
        bookmarkedBookstoreRepository.findByBookmarkIdAndBookstoreId(myInitBookmarkId, contentId)
            ?: throw BookandException(ErrorCode.NOT_FOUND_INIT_BOOKMARK_BOOKSTORE)
    }

    fun checkBookmarkedArticleInInitBookmark(myInitBookmarkId: Long, contentId: Long) {
        bookmarkedArticleRepository.findByBookmarkIdAndArticleId(myInitBookmarkId, contentId)
            ?: throw BookandException(ErrorCode.NOT_FOUND_INIT_BOOKMARK_ARTICLE)
    }

    fun existBookmarkedBookstore(bookmarkId: Long, contentId: Long) {
        val checkExist =
            bookmarkedBookstoreRepository.existsByBookmarkIdAndBookstoreId(bookmarkId, contentId)
        if (checkExist) throw BookandException(ErrorCode.ALREADY_EXIST_BOOKMARK)
    }

    fun checkBookmarkedBookstore(bookmarkId: Long, contentId: Long) {
        bookmarkedBookstoreRepository.findByBookmarkIdAndBookstoreId(bookmarkId, contentId)
            ?: throw BookandException(ErrorCode.ALREADY_EXIST_BOOKMARK)
    }

    fun existBookmarkedArticle(bookmarkId: Long, contentId: Long) {
        val checkExist =
            bookmarkedArticleRepository.existsByBookmarkIdAndArticleId(bookmarkId, contentId)
        if (checkExist) throw BookandException(ErrorCode.ALREADY_EXIST_BOOKMARK)
    }

    fun checkBookmarkedArticle(bookmarkId: Long, contentId: Long) {
        bookmarkedArticleRepository.findByBookmarkIdAndArticleId(bookmarkId, contentId)
            ?: throw BookandException(ErrorCode.NOT_FOUND_BOOKMARK_ARTICLE)
    }

    fun checkBookmarkFolderName(bookmark: Bookmark) {
        if (bookmark.folderName == INIT_BOOKMARK_FOLDER_NAME) {
            throw BookandException(ErrorCode.NOT_CHANGE_INIT_BOOKMARK)
        }
    }

    fun deleteBookmarkArticle(myBookmarkId: Long, articleId: Long) {
        bookmarkedArticleRepository.deleteByArticleIdAndBookmarkId(articleId, myBookmarkId)
    }

    fun deleteBookmarkBookstore(myBookmarkId: Long, bookstoreId: Long) {
        bookmarkedBookstoreRepository.deleteByBookstoreIdAndBookmarkId(bookstoreId, myBookmarkId)
    }

    fun createBookmarkArticle(myBookmark: Bookmark, article: Article, accountId: Long) {
        val account = accountService.getAccountById(accountId)
        val bookmarkArticle = BookmarkedArticle(
            bookmark = myBookmark,
            article = article,
            account = account
        )
        bookmarkedArticleRepository.save(bookmarkArticle)
        myBookmark.addBookmarkedArticle(bookmarkArticle)
    }

    fun createBookmarkBookstore(myBookmark: Bookmark, bookstore: Bookstore, accountId: Long) {
        val account = accountService.getAccountById(accountId)
        val bookmarkBookstore = BookmarkedBookstore(
            bookmark = myBookmark,
            bookstore = bookstore,
            account = account
        )
        bookmarkedBookstoreRepository.save(bookmarkBookstore)
        myBookmark.addBookmarkedBookstore(bookmarkBookstore)
    }

    private companion object {
        const val INIT_BOOKMARK_FOLDER_NAME = "모아보기"
    }

}