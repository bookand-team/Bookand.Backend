package kr.co.bookand.backend.bookstore.service

import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.bookstore.domain.*
import kr.co.bookand.backend.bookstore.domain.dto.*
import kr.co.bookand.backend.bookstore.exception.BookStoreException
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreImageRepository
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreRepository
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreThemeRepository
import kr.co.bookand.backend.bookstore.repository.KotlinReportBookstoreRepository
import kr.co.bookand.backend.common.domain.Status
import kr.co.bookand.backend.common.exception.ErrorCode
import lombok.RequiredArgsConstructor
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
    private val kotlinAccountService: KotlinAccountService
) {
    @Transactional
    fun createBookStore(id: Long, kotlinBookstoreRequest: KotlinBookstoreRequest): KotlinBookstore {
        kotlinAccountService.checkAccountAdmin(id)
        duplicateBookStoreName(kotlinBookstoreRequest.name)
        val themeList = kotlinBookstoreRequest.themeList
        val subImageList = kotlinBookstoreRequest.subImageList
        val bookStoreImageList: MutableList<KotlinBookstoreImage> = mutableListOf()
        val bookStoreThemeList: MutableList<KotlinBookstoreTheme> = mutableListOf()
        checkCountBookStoreTheme(themeList)
        subImageList
            .map { image -> KotlinBookstoreImage(url = image) }
            .forEach { bookStoreImage ->
                kotlinBookstoreImageRepository.save(bookStoreImage)
                bookStoreImageList.add(bookStoreImage)
            }

        themeList
            .map { theme -> KotlinBookstoreTheme(theme = BookStoreType.valueOf(theme)) }
            .forEach { bookStoreTheme ->
                kotlinBookstoreThemeRepository.save(bookStoreTheme)
                bookStoreThemeList.add(bookStoreTheme)
            }

        val bookStore = KotlinBookstore(kotlinBookstoreRequest)
        bookStoreImageList.forEach { bookStoreImage -> bookStoreImage.updateBookStore(bookStore) }
        bookStoreThemeList.forEach { bookStoreTheme -> bookStoreTheme.updateBookStore(bookStore) }
        return kotlinBookstoreRepository.save(bookStore)
    }

    @Transactional
    fun updateBookStore(bookstoreId: Long, kotlinBookstoreRequest: KotlinBookstoreRequest): KotlinBookstoreWebResponse {
        kotlinAccountService.checkAccountAdmin(bookstoreId)
        val bookstore = getBookstore(bookstoreId)
        val imageList = bookstore.imageList
        kotlinBookstoreImageRepository.deleteAll(imageList)
        for (image in kotlinBookstoreRequest.subImageList) {
            val bookStoreImage = KotlinBookstoreImage(url = image)
            kotlinBookstoreImageRepository.save(bookStoreImage)
            bookstore.updateBookStoreImage(bookStoreImage)
        }
        val themeList = bookstore.themeList
        kotlinBookstoreThemeRepository.deleteAll(themeList)
        for (theme in kotlinBookstoreRequest.themeList) {
            val bookStoreTheme = KotlinBookstoreTheme(theme = BookStoreType.valueOf(theme))
            kotlinBookstoreThemeRepository.save(bookStoreTheme)
            bookstore.updateBookStoreTheme(bookStoreTheme)
        }
        duplicateBookStoreName(kotlinBookstoreRequest.name)
        bookstore.updateBookStoreData(kotlinBookstoreRequest)

        return KotlinBookstoreWebResponse(
            id = bookstore.id,
            name = bookstore.name,
            address = bookstore.address,
            businessHours = bookstore.businessHours,
            contact = bookstore.contact,
            facility = bookstore.facility,
            sns = bookstore.sns,
            latitude = bookstore.latitude,
            longitude = bookstore.longitude,
            introduction = bookstore.introduction,
            mainImage = bookstore.mainImage,
            status = bookstore.status,
            themeList = bookstore.themeList.map { it.theme.name },
            subImageList = bookstore.imageList.map { it.url }
        )
    }
    @Transactional
    fun deleteBookStore(bookstoreId: Long) {
        val bookstore = getBookstore(bookstoreId)
        if (!bookstore.visibility) {
            throw RuntimeException("Bookstore already deleted")
        }
        bookstore.softDelete()
    }

    @Transactional
    fun deleteBookStoreList(request: KotlinBookstoreListRequest) {
        for (bookstoreId in request.bookstoreList) {
            deleteBookStore(bookstoreId)
        }
    }

    @Transactional
    fun updateBookstoreStatus(bookstoreId: Long, status: Status) : KotlinBookstore {
        val bookstore = getBookstore(bookstoreId)
        bookstore.updateBookStoreStatus(status)
        return bookstore
    }

    @Transactional
    fun createReportBookstore(request: KotlinReportBookstoreRequest): KotlinReportBookstore {
        val kotlinReportBookstore = KotlinReportBookstore(
            title = request.title,
            content = request.content,
            isAnswered = false,
            answerTitle = "",
            answerContent = "",
            answeredAt = "2023-01-01 00:00:00"
        )
        return kotlinReportBookstoreRepository.save(kotlinReportBookstore)
    }

    @Transactional
    fun answerReportBookstore(reportId: Long, request: KotlinAnswerReportRequest) {
        val reportBookstore = getReportBookstore(reportId)
        reportBookstore.updateAnswer(request)
    }

    fun duplicateBookStoreName(name: String) {
        if (kotlinBookstoreRepository.existsByName(name))
            throw BookStoreException(ErrorCode.DUPLICATE_BOOKSTORE_NAME, name)
    }

    fun checkCountBookStoreTheme(theme: List<String>) {
        if (theme.size > 4) {
            throw BookStoreException(ErrorCode.TOO_MANY_BOOKSTORE_THEME, theme.size)
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

}