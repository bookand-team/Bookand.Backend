package kr.co.bookand.backend.bookstore.service

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.bookmark.domain.BookmarkType
import kr.co.bookand.backend.bookmark.service.BookmarkService
import kr.co.bookand.backend.bookstore.domain.*
import kr.co.bookand.backend.bookstore.domain.dto.*
import kr.co.bookand.backend.bookstore.repository.BookstoreImageRepository
import kr.co.bookand.backend.bookstore.repository.BookstoreRepository
import kr.co.bookand.backend.bookstore.repository.BookstoreThemeRepository
import kr.co.bookand.backend.bookstore.repository.ReportBookstoreRepository
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.common.domain.MessageResponse
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class BookstoreService(
    private val bookstoreRepository: BookstoreRepository,
    private val bookstoreImageRepository: BookstoreImageRepository,
    private val bookstoreThemeRepository: BookstoreThemeRepository,
    private val reportBookstoreRepository: ReportBookstoreRepository,
    private val accountService: AccountService,
    private val bookmarkService: BookmarkService
) {

    @Transactional
    fun createBookstore(
        currentAccount: Account,
        bookstoreRequest: BookstoreRequest
    ): BookstoreIdResponse {
        currentAccount.role.checkAdminAndManager()
        duplicateBookstoreName(bookstoreRequest.name)
        val themeList = bookstoreRequest.themeList
        val subImageList = bookstoreRequest.subImageList
        val bookStoreImageList: MutableList<BookstoreImage> = mutableListOf()
        val bookStoreThemeList: MutableList<BookstoreTheme> = mutableListOf()
        checkCountBookstoreTheme(themeList)
        subImageList
            .map { image -> BookstoreImage(url = image) }
            .forEach { bookStoreImage ->
                bookstoreImageRepository.save(bookStoreImage)
                bookStoreImageList.add(bookStoreImage)
            }

        themeList
            .map { theme -> BookstoreTheme(theme = BookstoreType.valueOf(theme)) }
            .forEach { bookStoreTheme ->
                bookstoreThemeRepository.save(bookStoreTheme)
                bookStoreThemeList.add(bookStoreTheme)
            }

        val bookStore = Bookstore(bookstoreRequest)
        bookStoreImageList.forEach { bookStoreImage -> bookStoreImage.updateBookStore(bookStore) }
        bookStoreThemeList.forEach { bookStoreTheme -> bookStoreTheme.updateBookStore(bookStore) }
        val saveBookstore = bookstoreRepository.save(bookStore)
        return BookstoreIdResponse(saveBookstore.id)
    }

    @Transactional
    fun updateBookstore(
        bookstoreId: Long,
        bookstoreRequest: BookstoreRequest
    ): BookstoreWebResponse {
        accountService.checkAccountAdmin(bookstoreId)
        val bookstore = getBookstore(bookstoreId)
        val imageList = bookstore.imageList
        bookstoreImageRepository.deleteAll(imageList)
        bookstoreRequest.subImageList.forEach { image ->
            val bookStoreImage = BookstoreImage(url = image)
            bookstoreImageRepository.save(bookStoreImage)
            bookstore.updateBookstoreImage(bookStoreImage)
        }
        val themeList = bookstore.themeList
        bookstoreThemeRepository.deleteAll(themeList)
        bookstoreRequest.themeList.forEach { theme ->
            val bookStoreTheme = BookstoreTheme(theme = BookstoreType.valueOf(theme))
            bookstoreThemeRepository.save(bookStoreTheme)
            bookstore.updateBookstoreTheme(bookStoreTheme)
        }
        duplicateBookstoreName(bookstoreRequest.name)
        bookstore.updateBookstoreData(bookstoreRequest)

        return BookstoreWebResponse(bookstore)
    }

    @Transactional
    fun deleteBookstore(bookstoreId: Long): MessageResponse {
        val bookstore = getBookstore(bookstoreId)
        if (!bookstore.visibility) {
            throw RuntimeException("Bookstore already deleted")
        }
        bookstore.softDelete()
        return MessageResponse(message = "서점 삭제", statusCode = 200)
    }

    @Transactional
    fun deleteBookstoreList(request: BookstoreListRequest): MessageResponse {
        for (bookstoreId in request.bookstoreList) {
            deleteBookstore(bookstoreId)
        }
        return MessageResponse(message = "서점 삭제", statusCode = 200)
    }

    @Transactional
    fun updateBookstoreStatus(bookstoreId: Long): MessageResponse {
        val bookstore = getBookstore(bookstoreId)
        val status = bookstore.status
        if (status == Status.VISIBLE) bookstore.updateBookstoreStatus(Status.INVISIBLE)
        else bookstore.updateBookstoreStatus(Status.VISIBLE)
        return MessageResponse(message = "SUCCESS", statusCode = 200)
    }

    @Transactional
    fun createReportBookstore(request: ReportBookstoreRequest): ReportBookstoreIdResponse {
        val reportBookstore = ReportBookstore(
            name = request.name,
            address = request.address,
            isAnswered = false,
            answerTitle = "",
            answerContent = "",
            answeredAt = "2023-01-01 00:00:00"
        )
        val saveReportBookstore = reportBookstoreRepository.save(reportBookstore)
        return ReportBookstoreIdResponse(saveReportBookstore.id)

    }

    @Transactional
    fun answerReportBookstore(reportId: Long, request: AnswerReportRequest): MessageResponse {
        val reportBookstore = getReportBookstore(reportId)
        reportBookstore.updateAnswer(request)
        return MessageResponse(message = "SUCCESS", statusCode = 200)
    }

    fun duplicateBookstoreName(name: String) {
        if (bookstoreRepository.existsByName(name))
            throw RuntimeException(ErrorCode.DUPLICATE_BOOKSTORE_NAME.errorMessage)
    }

    fun checkCountBookstoreTheme(theme: List<String>) {
        if (theme.size > 4) {
            throw RuntimeException(ErrorCode.TOO_MANY_BOOKSTORE_THEME.errorMessage)
        }
    }

    fun getBookstoreResponse(bookstoreId: Long): BookstoreResponse {
        val bookstore = getBookstore(bookstoreId)
        return BookstoreResponse(bookstore)
    }

    fun getBookstore(id: Long): Bookstore {
        return bookstoreRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 서점입니다.") }
    }

    fun getReportBookstore(id: Long): ReportBookstore {
        return reportBookstoreRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 신고입니다.") }
    }

    fun getBookstoreSimpleList(currentAccount: Account, pageable: Pageable): BookstorePageResponse {
        val bookstorePage = bookstoreRepository.findAllByStatus(Status.VISIBLE, pageable)
            .map { bookstore ->
                val checkBookmark =
                    bookmarkService.checkBookmark(currentAccount.id, bookstore.id, BookmarkType.BOOKSTORE.name)
                return@map BookstoreSimpleResponse(bookstore, checkBookmark)
            }
        return BookstorePageResponse.of(bookstorePage)
    }

    fun searchBookstoreList(
        currentAccount: Account,
        searchKeyword: String?,
        theme: String?,
        status: String?,
        pageable: Pageable
    ): BookstoreWebPageResponse {
        val bookstorePage = bookstoreRepository
            .findAllBySearch(
                account = currentAccount,
                search = searchKeyword,
                theme = theme,
                status = status,
                pageable = pageable
            )
            .map { bookstore ->
                return@map BookstoreWebResponse(bookstore)
            }
        return BookstoreWebPageResponse.of(bookstorePage)
    }

    fun getWebBookstoreList(
        currentAccount: Account, pageable: Pageable
    ): BookstoreWebPageResponse {
        currentAccount.role.checkAdminAndManager()
        val bookstorePage = bookstoreRepository.findAll(pageable)
            .map { bookstore ->
                return@map BookstoreWebResponse(bookstore)
            }
        return BookstoreWebPageResponse.of(bookstorePage)
    }


    fun getBookstoreReportList(
        pageable: Pageable,
        currentAccount: Account
    ): ReportBookstoreListResponse {
        currentAccount.role.checkAdminAndManager()
        val reportBookstorePage = reportBookstoreRepository.findAll(pageable)
            .map { reportBookstore ->
                return@map ReportBookstoreResponse(reportBookstore)
            }

        return ReportBookstoreListResponse.of(reportBookstorePage)
    }

    fun getBookstoreAddressList(currentAccount: Account): BookstoreAddressListResponse {
        val bookStoreAddressListResponse = bookstoreRepository.findAllByStatus(Status.VISIBLE)
            .map { bookstore ->
                val checkBookmark =
                    bookmarkService.checkBookmark(currentAccount.id, bookstore.id, BookmarkType.BOOKSTORE.name)
                return@map BookstoreAddressResponse(bookstore, checkBookmark)
            }.toList()
        return BookstoreAddressListResponse(bookStoreAddressListResponse)
    }

}