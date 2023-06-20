package kr.co.bookand.backend.bookstore.service

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkType
import kr.co.bookand.backend.bookmark.service.KotlinBookmarkService
import kr.co.bookand.backend.bookstore.domain.*
import kr.co.bookand.backend.bookstore.domain.dto.*
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreImageRepository
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreRepository
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreThemeRepository
import kr.co.bookand.backend.bookstore.repository.KotlinReportBookstoreRepository
import kr.co.bookand.backend.common.KotlinErrorCode
import kr.co.bookand.backend.common.KotlinStatus
import kr.co.bookand.backend.common.domain.KotlinMessageResponse
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class KotlinBookstoreService(
    private val kotlinBookstoreRepository: KotlinBookstoreRepository,
    private val kotlinBookstoreImageRepository: KotlinBookstoreImageRepository,
    private val kotlinBookstoreThemeRepository: KotlinBookstoreThemeRepository,
    private val kotlinReportBookstoreRepository: KotlinReportBookstoreRepository,
    private val kotlinAccountService: KotlinAccountService,
    private val kotlinBookmarkService: KotlinBookmarkService
) {

    @Transactional
    fun createBookstore(
        currentAccount: KotlinAccount,
        kotlinBookstoreRequest: KotlinBookstoreRequest
    ): KotlinBookstoreIdResponse {
        currentAccount.role.checkAdminAndManager()
        duplicateBookstoreName(kotlinBookstoreRequest.name)
        val themeList = kotlinBookstoreRequest.themeList
        val subImageList = kotlinBookstoreRequest.subImageList
        val bookStoreImageList: MutableList<KotlinBookstoreImage> = mutableListOf()
        val bookStoreThemeList: MutableList<KotlinBookstoreTheme> = mutableListOf()
        checkCountBookstoreTheme(themeList)
        subImageList
            .map { image -> KotlinBookstoreImage(url = image) }
            .forEach { bookStoreImage ->
                kotlinBookstoreImageRepository.save(bookStoreImage)
                bookStoreImageList.add(bookStoreImage)
            }

        themeList
            .map { theme -> KotlinBookstoreTheme(theme = KotlinBookstoreType.valueOf(theme)) }
            .forEach { bookStoreTheme ->
                kotlinBookstoreThemeRepository.save(bookStoreTheme)
                bookStoreThemeList.add(bookStoreTheme)
            }

        val bookStore = KotlinBookstore(kotlinBookstoreRequest)
        bookStoreImageList.forEach { bookStoreImage -> bookStoreImage.updateBookStore(bookStore) }
        bookStoreThemeList.forEach { bookStoreTheme -> bookStoreTheme.updateBookStore(bookStore) }
        val saveBookstore = kotlinBookstoreRepository.save(bookStore)
        return KotlinBookstoreIdResponse(saveBookstore.id)
    }

    @Transactional
    fun updateBookstore(
        bookstoreId: Long,
        kotlinBookstoreRequest: KotlinBookstoreRequest
    ): KotlinWebBookstoreResponse {
        kotlinAccountService.checkAccountAdmin(bookstoreId)
        val bookstore = getBookstore(bookstoreId)
        val imageList = bookstore.imageList
        kotlinBookstoreImageRepository.deleteAll(imageList)
        kotlinBookstoreRequest.subImageList.forEach { image ->
            val bookStoreImage = KotlinBookstoreImage(url = image)
            kotlinBookstoreImageRepository.save(bookStoreImage)
            bookstore.updateBookstoreImage(bookStoreImage)
        }
        val themeList = bookstore.themeList
        kotlinBookstoreThemeRepository.deleteAll(themeList)
        kotlinBookstoreRequest.themeList.forEach { theme ->
            val bookStoreTheme = KotlinBookstoreTheme(theme = KotlinBookstoreType.valueOf(theme))
            kotlinBookstoreThemeRepository.save(bookStoreTheme)
            bookstore.updateBookstoreTheme(bookStoreTheme)
        }
        duplicateBookstoreName(kotlinBookstoreRequest.name)
        bookstore.updateBookstoreData(kotlinBookstoreRequest)

        return KotlinWebBookstoreResponse(bookstore)
    }

    @Transactional
    fun deleteBookstore(bookstoreId: Long): KotlinMessageResponse {
        val bookstore = getBookstore(bookstoreId)
        if (!bookstore.visibility) {
            throw RuntimeException("Bookstore already deleted")
        }
        bookstore.softDelete()
        return KotlinMessageResponse(message = "서점 삭제", statusCode = 200)
    }

    @Transactional
    fun deleteBookstoreList(request: KotlinBookstoreListRequest): KotlinMessageResponse {
        for (bookstoreId in request.bookstoreList) {
            deleteBookstore(bookstoreId)
        }
        return KotlinMessageResponse(message = "서점 삭제", statusCode = 200)
    }

    @Transactional
    fun updateBookstoreStatus(bookstoreId: Long): KotlinMessageResponse {
        val bookstore = getBookstore(bookstoreId)
        val status = bookstore.status
        if (status == KotlinStatus.VISIBLE) bookstore.updateBookstoreStatus(KotlinStatus.INVISIBLE)
        else bookstore.updateBookstoreStatus(KotlinStatus.VISIBLE)
        return KotlinMessageResponse(message = "SUCCESS", statusCode = 200)
    }

    @Transactional
    fun createReportBookstore(request: KotlinReportBookstoreRequest): KotlinReportBookstoreIdResponse {
        val kotlinReportBookstore = KotlinReportBookstore(
            name = request.name,
            address = request.address,
            isAnswered = false,
            answerTitle = "",
            answerContent = "",
            answeredAt = "2023-01-01 00:00:00"
        )
        val saveReportBookstore = kotlinReportBookstoreRepository.save(kotlinReportBookstore)
        return KotlinReportBookstoreIdResponse(saveReportBookstore.id)

    }

    @Transactional
    fun answerReportBookstore(reportId: Long, request: KotlinAnswerReportRequest): KotlinMessageResponse {
        val reportBookstore = getReportBookstore(reportId)
        reportBookstore.updateAnswer(request)
        return KotlinMessageResponse(message = "SUCCESS", statusCode = 200)
    }

    fun duplicateBookstoreName(name: String) {
        if (kotlinBookstoreRepository.existsByName(name))
            throw RuntimeException(KotlinErrorCode.DUPLICATE_BOOKSTORE_NAME.errorMessage)
    }

    fun checkCountBookstoreTheme(theme: List<String>) {
        if (theme.size > 4) {
            throw RuntimeException(KotlinErrorCode.TOO_MANY_BOOKSTORE_THEME.errorMessage)
        }
    }

    fun getBookstore(id: Long): KotlinBookstore {
        return kotlinBookstoreRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 서점입니다.") }
    }

    fun getReportBookstore(id: Long): KotlinReportBookstore {
        return kotlinReportBookstoreRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 신고입니다.") }
    }

    fun getBookstoreSimpleList(currentAccount: KotlinAccount, pageable: Pageable): KotlinBookstorePageResponse {
        val bookstorePage = kotlinBookstoreRepository.findAllByStatus(KotlinStatus.VISIBLE, pageable)
            .map { bookstore ->
                val checkBookmark =
                    kotlinBookmarkService.checkBookmark(currentAccount.id, bookstore.id, KotlinBookmarkType.BOOKSTORE.name)
                return@map KotlinBookstoreSimpleResponse(bookstore, checkBookmark)
            }
        return KotlinBookstorePageResponse.of(bookstorePage)
    }

    fun searchBookstoreList(
        currentAccount: KotlinAccount,
        searchKeyword: String?,
        theme: String?,
        status: String?,
        pageable: Pageable
    ): KotlinWebBookstorePageResponse {
        val bookstorePage = kotlinBookstoreRepository
            .findAllBySearch(
                account = currentAccount,
                search = searchKeyword,
                theme = theme,
                status = status,
                pageable = pageable
            )
            .map { bookstore ->
                return@map KotlinWebBookstoreResponse(bookstore)
            }
        return KotlinWebBookstorePageResponse.of(bookstorePage)
    }

    fun getWebBookstoreList(
        currentAccount: KotlinAccount, pageable: Pageable
    ): KotlinWebBookstorePageResponse {
        currentAccount.role.checkAdminAndManager()
        val bookstorePage = kotlinBookstoreRepository.findAll(pageable)
            .map { bookstore ->
                return@map KotlinWebBookstoreResponse(bookstore)
            }
        return KotlinWebBookstorePageResponse.of(bookstorePage)
    }


    fun getBookstoreReportList(
        pageable: Pageable,
        currentAccount: KotlinAccount
    ): KotlinReportBookstoreListResponse {

        val reportBookstorePage = kotlinReportBookstoreRepository.findAll(pageable)
            .map { reportBookstore ->
                return@map KotlinReportBookstoreResponse(reportBookstore)
            }

        return KotlinReportBookstoreListResponse.of(reportBookstorePage)
    }

    fun getBookstoreAddressList(currentAccount: KotlinAccount): KotlinBookStoreAddressListResponse {
        val bookStoreAddressListResponse = kotlinBookstoreRepository.findAllByStatus(KotlinStatus.VISIBLE)
            .map { bookstore ->
                val checkBookmark =
                    kotlinBookmarkService.checkBookmark(currentAccount.id, bookstore.id, KotlinBookmarkType.BOOKSTORE.name)
                return@map KotlinBookstoreAddressResponse(bookstore, checkBookmark)
            }.toList()
        return KotlinBookStoreAddressListResponse(bookStoreAddressListResponse)
    }

}