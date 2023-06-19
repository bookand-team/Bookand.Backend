package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.domain.KotlinAccountStatus
import kr.co.bookand.backend.account.domain.KotlinRole
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.bookmark.service.KotlinBookmarkService
import kr.co.bookand.backend.bookstore.domain.*
import kr.co.bookand.backend.bookstore.domain.dto.*
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreImageRepository
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreRepository
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreThemeRepository
import kr.co.bookand.backend.bookstore.repository.KotlinReportBookstoreRepository
import kr.co.bookand.backend.bookstore.service.KotlinBookstoreService
import kr.co.bookand.backend.common.KotlinStatus
import java.time.LocalDateTime
import java.util.*

class BookstoreServiceTest : BehaviorSpec({
    val bookstoreRepository = mockk<KotlinBookstoreRepository>()
    val bookstoreImageRepository = mockk<KotlinBookstoreImageRepository>()
    val bookstoreThemeRepository = mockk<KotlinBookstoreThemeRepository>()
    val reportBookstoreRepository = mockk<KotlinReportBookstoreRepository>()
    val accountService = mockk<KotlinAccountService>()
    val bookmarkService = mockk<KotlinBookmarkService>()

    val bookstoreService = KotlinBookstoreService(
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

        val account = KotlinAccount(
            2L,
            "email@email.com",
            "password",
            "nickname",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            KotlinRole.USER,
            KotlinAccountStatus.NORMAL
        )

        val adminAccount = KotlinAccount(
            1L,
            "admin@email.com",
            "password",
            "admin",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            KotlinRole.ADMIN,
            KotlinAccountStatus.NORMAL
        )


        val bookstore = KotlinBookstore(
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
            KotlinStatus.VISIBLE,
            0,
            0,
            LocalDateTime.now()
        )

        val bookstore2 = KotlinBookstore(
            2L,
            "name2",
            "address2",
            "businessHours2",
            "contact2",
            "facility2",
            "sns2",
            "latitude2",
            "longitude2",
            "introduction2",
            "mainImage2",
            KotlinStatus.VISIBLE,
            0,
            0,
            LocalDateTime.now()
        )

        val bookstore3 = KotlinBookstore(
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
            KotlinStatus.VISIBLE,
            0,
            0,
            LocalDateTime.now()
        )

        val bookstore4 = KotlinBookstore(
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
            KotlinStatus.VISIBLE,
            0,
            0,
            LocalDateTime.now()
        )

        val bookstoreImage = KotlinBookstoreImage(
            1L,
            "imageUrl",
            bookstore
        )

        val bookstoreTheme1 = KotlinBookstoreTheme(
            1L,
            KotlinBookstoreType.TRAVEL,
            bookstore
        )

        val bookstoreTheme2 = KotlinBookstoreTheme(
            2L,
            KotlinBookstoreType.MUSIC,
            bookstore
        )

        val reportBookstore = KotlinReportBookstore(
            1L,
            "title",
            "address",
            false,
            "answerTitle",
            "answerContent",
            "2023-01-01 00:00:00"
        )

        val bookstoreRequest = KotlinBookstoreRequest(
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

        val bookstoreRequest2 = KotlinBookstoreRequest(
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

        val existingSubImages = listOf(
            KotlinBookstoreImage(id = 1L, url = "old_image1.jpg", bookstore = bookstore),
            KotlinBookstoreImage(id = 2L, url = "old_image2.jpg", bookstore = bookstore2)
        )
        val existingThemes = listOf(
            KotlinBookstoreTheme(id = 1L, theme = KotlinBookstoreType.TRAVEL, bookstore = bookstore),
            KotlinBookstoreTheme(id = 2L, theme = KotlinBookstoreType.PICTURE, bookstore = bookstore2)
        )

        val bookstoreListRequest = KotlinBookstoreListRequest(
            listOf(1L, 2L)
        )

        val bookstoreListRequest2 = KotlinBookstoreListRequest(
            listOf(3L, 4L)
        )

        val reportBookstoreRequest = KotlinReportBookstoreRequest(
            name = "name",
            address = "address",
        )

        val answerReportRequest = KotlinAnswerReportRequest(
            answerTitle = "answerTitle",
            answerContent = "answerContent"
        )

        val createBookstore = KotlinBookstore(
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
            KotlinStatus.VISIBLE,
            0,
            0,
            LocalDateTime.now(),
            mutableListOf(bookstoreTheme1, bookstoreTheme2),
            mutableListOf(bookstoreImage)
        )


        When("account is not Admin") {
            every { accountService.checkAccountAdmin(2L) } throws RuntimeException("Not admin")

            val exception = shouldThrow<RuntimeException> {
                bookstoreService.createBookStore(account, bookstoreRequest)
            }

            Then("it should throw exception") {
                exception.message shouldBe "KotlinErrorCode.ROLE_ACCESS_ERROR"
            }
        }

        When("too many book store themes") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.findById(1L) } returns Optional.of(bookstore)
            every { bookstoreRepository.existsByName("Book Store") } returns false

            val exception = shouldThrow<RuntimeException> {
                val createBookStore = bookstoreService.createBookStore(adminAccount, bookstoreRequest2)
                println("createBookStore = $createBookStore")
            }

            Then("it should throw an exception") {

                exception.message shouldBe "TOO MANY BOOKSTORE THEME"
            }
        }


        When("bookstore 생성") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.save(any()) } returns createBookstore
            every { bookstoreImageRepository.save(any()) } returns bookstoreImage
            every { bookstoreThemeRepository.save(match { it.theme == KotlinBookstoreType.TRAVEL }) } returns bookstoreTheme1
            every { bookstoreThemeRepository.save(match { it.theme == KotlinBookstoreType.MUSIC }) } returns bookstoreTheme2
            every { bookstoreRepository.existsByName("Book Store") } returns false

            val createBookstore = bookstoreService.createBookStore(adminAccount, bookstoreRequest)

                Then("it should return created book store") {
                    createBookstore.id shouldBe 1L
                }
        }

        When("bookstore 업데이트") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.findById(bookstoreId) } returns Optional.of(bookstore)
            every { bookstoreImageRepository.deleteAll(bookstore.imageList) } returns Unit
            every { bookstoreThemeRepository.deleteAll(bookstore.themeList) } returns Unit
            every { bookstoreImageRepository.save(any()) } returns bookstoreImage
            every { bookstoreThemeRepository.save(match { it.theme == KotlinBookstoreType.TRAVEL }) } returns bookstoreTheme1
            every { bookstoreThemeRepository.save(match { it.theme == KotlinBookstoreType.MUSIC }) } returns bookstoreTheme2
            every { bookstoreRepository.existsByName("Book Store") } returns false

            // When
            val response = bookstoreService.updateBookStore(bookstoreId, bookstoreRequest)

            Then("it should return updated book store") {
                response shouldBe KotlinWebBookstoreResponse(
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
                    status = KotlinStatus.VISIBLE,
                    themeList = bookstoreRequest.themeList,
                    subImageList = bookstoreRequest.subImageList
                )
            }
        }

        When("bookstore 삭제") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.findById(bookstoreId) } returns Optional.of(bookstore)

            bookstoreService.deleteBookStore(bookstoreId)

            Then("it should delete book store") {
                bookstore.visibility shouldBe false
            }

            Then("throw exception") {
                val exception = shouldThrow<RuntimeException> {
                    bookstoreService.deleteBookStore(bookstoreId)
                }
                exception.message shouldBe "Bookstore already deleted"
            }
        }

        When("bookstore List 삭제") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.findById(3L) } returns Optional.of(bookstore3)
            every { bookstoreRepository.findById(4L) } returns Optional.of(bookstore4)

            bookstoreService.deleteBookStoreList(bookstoreListRequest2)

            Then("it should delete book store") {
                bookstore3.visibility shouldBe false
                bookstore4.visibility shouldBe false
            }

            Then("throw exception") {
                val exception = shouldThrow<RuntimeException> {
                    bookstoreService.deleteBookStoreList(bookstoreListRequest)
                }
                exception.message shouldBe "Bookstore already deleted"
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
                if (beforeBookstoreStatus == KotlinStatus.VISIBLE) {
                    bookstore.status shouldBe KotlinStatus.INVISIBLE
                } else {
                    bookstore.status shouldBe KotlinStatus.VISIBLE
                }
            }
        }

        When("bookstore report") {
            every { accountService.checkAccountUser(accountId) } returns Unit
            every { reportBookstoreRepository.save(any()) } returns reportBookstore

            val saveReportBookstore = bookstoreService.createReportBookstore(reportBookstoreRequest)

            Then("it should report bookstore") {
                saveReportBookstore.id shouldBe 1L
            }
        }

        When("bookstore report answer") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { reportBookstoreRepository.findById(1L) } returns Optional.of(reportBookstore)

            bookstoreService.answerReportBookstore(1L, answerReportRequest)

            Then("it should answer bookstore report") {
                reportBookstore.isAnswered shouldBe true
                reportBookstore.answerTitle shouldBe answerReportRequest.answerTitle
                reportBookstore.answerContent shouldBe answerReportRequest.answerContent
            }
        }
    }
})