package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.account.model.AccountStatus
import kr.co.bookand.backend.account.model.Role
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.bookmark.service.BookmarkService
import kr.co.bookand.backend.bookstore.dto.*
import kr.co.bookand.backend.bookstore.model.*
import kr.co.bookand.backend.bookstore.repository.BookstoreImageRepository
import kr.co.bookand.backend.bookstore.repository.BookstoreRepository
import kr.co.bookand.backend.bookstore.repository.BookstoreThemeRepository
import kr.co.bookand.backend.bookstore.repository.ReportBookstoreRepository
import kr.co.bookand.backend.bookstore.service.BookstoreService
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.common.exception.BookandException
import java.time.LocalDateTime
import java.util.*

class BookstoreServiceTest : BehaviorSpec({
    val bookstoreRepository = mockk<BookstoreRepository>()
    val bookstoreImageRepository = mockk<BookstoreImageRepository>()
    val bookstoreThemeRepository = mockk<BookstoreThemeRepository>()
    val reportBookstoreRepository = mockk<ReportBookstoreRepository>()
    val accountService = mockk<AccountService>()
    val bookmarkService = mockk<BookmarkService>()

    val bookstoreService = BookstoreService(
        bookstoreRepository,
        bookstoreImageRepository,
        bookstoreThemeRepository,
        reportBookstoreRepository,
        accountService,
        bookmarkService
    )

    Given("bookstore Test") {
        val accountId = 1L
        val bookstoreId = 1L

        val account = Account(
            2L,
            "email@email.com",
            "password",
            "nickname",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            Role.USER,
            AccountStatus.NORMAL
        )

        val adminAccount = Account(
            1L,
            "admin@email.com",
            "password",
            "admin",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            Role.ADMIN,
            AccountStatus.NORMAL
        )


        val bookstore = Bookstore(
            1L,
            "name",
            "address",
            "businessHours",
            "contact",
            "facility",
            "sns",
            "latitude",
            "longitude",
            "introduction",
            "mainImage",
            Status.VISIBLE,
            0,
            0,
            LocalDateTime.now()
        )

        val bookstore3 = Bookstore(
            3L,
            "name",
            "address",
            "businessHours",
            "contact",
            "facility",
            "sns",
            "latitude",
            "longitude",
            "introduction",
            "mainImage",
            Status.VISIBLE,
            0,
            0,
            LocalDateTime.now()
        )

        val bookstore4 = Bookstore(
            4L,
            "name",
            "address",
            "businessHours",
            "contact",
            "facility",
            "sns",
            "latitude",
            "longitude",
            "introduction",
            "mainImage",
            Status.VISIBLE,
            0,
            0,
            LocalDateTime.now()
        )

        val bookstoreImage = BookstoreImage(
            1L,
            "imageUrl",
            bookstore
        )

        val bookstoreTheme1 = BookstoreTheme(
            1L,
            BookstoreType.TRAVEL,
            bookstore
        )

        val bookstoreTheme2 = BookstoreTheme(
            2L,
            BookstoreType.MUSIC,
            bookstore
        )

        val reportBookstore = ReportBookstore(
            1L,
            "title",
            "address",
            false,
            "answerTitle",
            "answerContent",
            "2023-01-01 00:00:00"
        )

        val bookstoreRequest = BookstoreRequest(
            name = "Book Store",
            address = "Address",
            businessHours = "Business Hours",
            contact = "Contact",
            facility = "Facility",
            sns = "SNS",
            latitude = "latitude",
            longitude = "longitude",
            introduction = "Introduction",
            mainImage = "Main Image",
            themeList = listOf("TRAVEL", "MUSIC"),
            subImageList = listOf("imageUrl")
        )

        val bookstoreRequest2 = BookstoreRequest(
            name = "Book Store",
            address = "Address",
            businessHours = "Business Hours",
            contact = "Contact",
            facility = "Facility",
            sns = "SNS",
            latitude = "latitude",
            longitude = "longitude",
            introduction = "Introduction",
            mainImage = "Main Image",
            themeList = listOf("TRAVEL", "MUSIC", "MUSIC", "MUSIC", "MUSIC"),
            subImageList = listOf("imageUrl", "imageUrl2")
        )

        val bookstoreListRequest = BookstoreListRequest(
            listOf(1L, 2L)
        )

        val bookstoreListRequest2 = BookstoreListRequest(
            listOf(3L, 4L)
        )

        val reportBookstoreRequest = ReportBookstoreRequest(
            name = "name",
            address = "address",
        )

        val answerReportRequest = AnswerReportRequest(
            answerTitle = "answerTitle",
            answerContent = "answerContent"
        )

        val createBookstore = Bookstore(
            1L,
            "name",
            "address",
            "businessHours",
            "contact",
            "facility",
            "sns",
            "latitude",
            "longitude",
            "introduction",
            "mainImage",
            Status.VISIBLE,
            0,
            0,
            LocalDateTime.now(),
            mutableListOf(bookstoreTheme1, bookstoreTheme2),
            mutableListOf(bookstoreImage)
        )


        When("account is not Admin") {
            every { accountService.checkAccountAdmin(2L) } throws BookandException(ErrorCode.ROLE_ACCESS_ERROR)

            val exception = shouldThrow<BookandException> {
                bookstoreService.createBookstore(account, bookstoreRequest)
            }

            Then("it should throw exception") {
                exception.message shouldBe ErrorCode.ROLE_ACCESS_ERROR.errorLog
            }
        }

        When("too many book store themes") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.findById(1L) } returns Optional.of(bookstore)
            every { bookstoreRepository.existsByName("Book Store") } returns false

            val exception = shouldThrow<RuntimeException> {
                val createBookStore = bookstoreService.createBookstore(adminAccount, bookstoreRequest2)
                println("createBookStore = $createBookStore")
            }

            Then("it should throw an exception") {

                exception.message shouldBe ErrorCode.TOO_MANY_BOOKSTORE_THEME.errorLog
            }
        }


        When("bookstore 생성") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.save(any()) } returns createBookstore
            every { bookstoreImageRepository.save(any()) } returns bookstoreImage
            every { bookstoreThemeRepository.save(match { it.theme == BookstoreType.TRAVEL }) } returns bookstoreTheme1
            every { bookstoreThemeRepository.save(match { it.theme == BookstoreType.MUSIC }) } returns bookstoreTheme2
            every { bookstoreRepository.existsByName("Book Store") } returns false

            val saveBookstore = bookstoreService.createBookstore(adminAccount, bookstoreRequest)

                Then("it should return created book store") {
                    saveBookstore.id shouldBe 1L
                }
        }

        When("bookstore 업데이트") {
            every { bookstoreRepository.findById(bookstoreId) } returns Optional.of(bookstore)
            every { bookstoreImageRepository.deleteAll(bookstore.imageList) } returns Unit
            every { bookstoreThemeRepository.deleteAll(bookstore.themeList) } returns Unit
            every { bookstoreImageRepository.save(any()) } returns bookstoreImage
            every { bookstoreThemeRepository.save(match { it.theme == BookstoreType.TRAVEL }) } returns bookstoreTheme1
            every { bookstoreThemeRepository.save(match { it.theme == BookstoreType.MUSIC }) } returns bookstoreTheme2
            every { bookstoreRepository.existsByName("Book Store") } returns false

            // When
            val response = bookstoreService.updateBookstore(adminAccount, bookstoreId, bookstoreRequest)

            Then("it should return updated book store") {
                response shouldBe BookstoreWebResponse(
                    id = bookstoreId,
                    name = bookstoreRequest.name,
                    address = bookstoreRequest.address,
                    businessHours = bookstoreRequest.businessHours,
                    contact = bookstoreRequest.contact,
                    facility = bookstoreRequest.facility,
                    sns = bookstoreRequest.sns,
                    latitude = bookstoreRequest.latitude,
                    longitude = bookstoreRequest.longitude,
                    introduction = bookstoreRequest.introduction,
                    mainImage = bookstoreRequest.mainImage,
                    status = Status.VISIBLE,
                    themeList = bookstoreRequest.themeList,
                    subImageList = bookstoreRequest.subImageList
                )
            }
        }

        When("bookstore 삭제") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.findById(bookstoreId) } returns Optional.of(bookstore)

            bookstoreService.deleteBookstore(bookstoreId)

            Then("it should delete book store") {
                bookstore.visibility shouldBe false
            }

            Then("throw exception") {
                val exception = shouldThrow<RuntimeException> {
                    bookstoreService.deleteBookstore(bookstoreId)
                }
                exception.message shouldBe ErrorCode.ALREADY_DELETE_BOOKSTORE.errorLog
            }
        }

        When("bookstore List 삭제") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.findById(3L) } returns Optional.of(bookstore3)
            every { bookstoreRepository.findById(4L) } returns Optional.of(bookstore4)

            bookstoreService.deleteBookstoreList(bookstoreListRequest2)

            Then("it should delete book store") {
                bookstore3.visibility shouldBe false
                bookstore4.visibility shouldBe false
            }

            Then("throw exception") {
                val exception = shouldThrow<RuntimeException> {
                    bookstoreService.deleteBookstoreList(bookstoreListRequest)
                }
                exception.message shouldBe ErrorCode.ALREADY_DELETE_BOOKSTORE.errorLog
            }
        }

        When("bookstore 조회 (id)") {
            every { bookstoreRepository.findById(bookstoreId) } returns Optional.of(bookstore)

            val result = bookstoreService.getBookstore(bookstoreId)
            Then("it should return bookstore") {
                result.name shouldBe bookstore.name
            }
        }

        When("bookstore Status 업데이트") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.findById(bookstoreId) } returns Optional.of(bookstore)
            val beforeBookstoreStatus = bookstore.status
            bookstoreService.updateBookstoreStatus(bookstoreId)

            Then("it should update bookstore status") {
                if (beforeBookstoreStatus == Status.VISIBLE) {
                    bookstore.status shouldBe Status.INVISIBLE
                } else {
                    bookstore.status shouldBe Status.VISIBLE
                }
            }
        }

        When("bookstore report") {
            every { accountService.checkAccountUser(accountId) } returns Unit
            every { reportBookstoreRepository.save(any()) } returns reportBookstore

            val saveReportBookstore = bookstoreService.createReportBookstore(reportBookstoreRequest)

            Then("it should report bookstore") {
                saveReportBookstore.result shouldBe "서점 제보 완료"
            }
        }

        When("bookstore report answer") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { reportBookstoreRepository.findById(1L) } returns Optional.of(reportBookstore)

            bookstoreService.answerReportBookstore(1L, answerReportRequest)

            Then("it should answer bookstore report") {
                reportBookstore.checkAnswered shouldBe true
                reportBookstore.answerTitle shouldBe answerReportRequest.answerTitle
                reportBookstore.answerContent shouldBe answerReportRequest.answerContent
            }
        }
    }
})