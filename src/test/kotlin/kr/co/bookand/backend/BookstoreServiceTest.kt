package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.bookstore.domain.*
import kr.co.bookand.backend.bookstore.domain.dto.*
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreImageRepository
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreRepository
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreThemeRepository
import kr.co.bookand.backend.bookstore.repository.KotlinReportBookstoreRepository
import kr.co.bookand.backend.bookstore.service.KotlinBookstoreService
import kr.co.bookand.backend.common.domain.Status
import java.time.LocalDateTime
import java.util.*

class BookstoreServiceTest : BehaviorSpec({
    val bookstoreRepository = mockk<KotlinBookstoreRepository>()
    val bookstoreImageRepository = mockk<KotlinBookstoreImageRepository>()
    val bookstoreThemeRepository = mockk<KotlinBookstoreThemeRepository>()
    val reportBookstoreRepository = mockk<KotlinReportBookstoreRepository>()
    val accountService = mockk<KotlinAccountService>()

    val bookstoreService = KotlinBookstoreService(
        bookstoreRepository,
        bookstoreImageRepository,
        bookstoreThemeRepository,
        reportBookstoreRepository,
        accountService
    )

    Given("bookstore Test") {
        val accountId = 1L
        val bookstoreId = 1L
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
            Status.VISIBLE,
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
            Status.VISIBLE,
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
            Status.VISIBLE,
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
            Status.VISIBLE,
            0,
            0,
            LocalDateTime.now()
        )

        val bookstoreImage = KotlinBookstoreImage(
            1L,
            "imageUrl",
            bookstore
        )

        val bookstoreImage2 = KotlinBookstoreImage(
            2L,
            "imageUrl2",
            bookstore
        )


        val bookstoreTheme11 = KotlinBookstoreTheme(
            1L,
            BookStoreType.TRAVEL,
            bookstore
        )

        val bookstoreTheme22 = KotlinBookstoreTheme(
            2L,
            BookStoreType.MUSIC,
            bookstore
        )

        val bookstoreTheme1 = KotlinBookstoreTheme(theme = BookStoreType.valueOf("TRAVEL"))
        val bookstoreTheme2 = KotlinBookstoreTheme(theme = BookStoreType.valueOf("MUSIC"))

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
            KotlinBookstoreImage(id = 1L, url = "old_image1.jpg", bookStore = bookstore),
            KotlinBookstoreImage(id = 2L, url = "old_image2.jpg", bookStore = bookstore2)
        )
        val existingThemes = listOf(
            KotlinBookstoreTheme(id = 1L, theme = BookStoreType.TRAVEL, bookStore = bookstore),
            KotlinBookstoreTheme(id = 2L, theme = BookStoreType.PICTURE, bookStore = bookstore2)
        )

        val bookstoreListRequest = KotlinBookstoreListRequest(
            listOf(1L, 2L)
        )

        val bookstoreListRequest2 = KotlinBookstoreListRequest(
            listOf(3L, 4L)
        )

        val reportBookstoreRequest = KotlinReportBookstoreRequest(
            title = "title",
            content = "content",
            isAnswered = true,
            answerTitle = "answerTitle",
            answerContent = "answerContent",
            answeredAt = "2023/01/01 00:00:00",
        )

        val answerReportRequest = KotlinAnswerReportRequest(
            isAnswered = true,
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
            Status.VISIBLE,
            0,
            0,
            LocalDateTime.now(),
            mutableListOf(bookstoreTheme11, bookstoreTheme22),
            mutableListOf(bookstoreImage)
        )


        When("account is not Admin") {
            every { accountService.checkAccountAdmin(2L) } throws RuntimeException("Not admin")

            val exception = shouldThrow<RuntimeException> {
                bookstoreService.createBookStore(2L, bookstoreRequest)
            }

            Then("it should throw exception") {
                exception.message shouldBe "Not admin"
            }
        }

        When("too many book store themes") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.findById(1L) } returns Optional.of(bookstore)
            every { bookstoreRepository.existsByName("Book Store") } returns false

            val exception = shouldThrow<RuntimeException> {
                val createBookStore = bookstoreService.createBookStore(1L, bookstoreRequest2)
                println("createBookStore = ${createBookStore}")
            }

            Then("it should throw an exception") {

                exception.message shouldBe "TOO MANY BOOKSTORE THEME"
            }
        }


        When("bookstore 생성") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.save(any()) } returns createBookstore
            every { bookstoreImageRepository.save(any()) } returns bookstoreImage
            every { bookstoreThemeRepository.save(match { it.theme == BookStoreType.TRAVEL }) } returns bookstoreTheme11
            every { bookstoreThemeRepository.save(match { it.theme == BookStoreType.MUSIC }) } returns bookstoreTheme22
            every { bookstoreRepository.existsByName("Book Store") } returns false

            val createBookstore = bookstoreService.createBookStore(1L, bookstoreRequest)


            val kotlinBookstoreResponse = KotlinBookstoreResponse(
                createBookstore.id,
                createBookstore.name,
                createBookstore.address,
                createBookstore.latitude,
                createBookstore.longitude,
            )

            Then("it should return bookstore") {
                kotlinBookstoreResponse shouldBe KotlinBookstoreResponse(
                    id = bookstore.id,
                    name = bookstore.name,
                    address = bookstore.address,
                    latitude = bookstore.latitude,
                    longitude = bookstore.longitude,
                )
            }
        }

        When("bookstore 업데이트") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { bookstoreRepository.findById(bookstoreId) } returns Optional.of(bookstore)
            every { bookstoreImageRepository.deleteAll(bookstore.imageList) } returns Unit
            every { bookstoreThemeRepository.deleteAll(bookstore.themeList) } returns Unit
            every { bookstoreImageRepository.save(any()) } returns bookstoreImage
            every { bookstoreThemeRepository.save(match { it.theme == BookStoreType.TRAVEL }) } returns bookstoreTheme11
            every { bookstoreThemeRepository.save(match { it.theme == BookStoreType.MUSIC }) } returns bookstoreTheme22
            every { bookstoreRepository.existsByName("Book Store") } returns false

            // When
            val response = bookstoreService.updateBookStore(bookstoreId, bookstoreRequest)

            Then("it should return updated book store") {
                response shouldBe KotlinBookstoreWebResponse(
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
            bookstoreService.updateBookstoreStatus(bookstoreId, Status.VISIBLE)

            Then("it should update bookstore status") {
                if (beforeBookstoreStatus == Status.VISIBLE) {
                    bookstore.status shouldBe Status.VISIBLE
                } else {
                    bookstore.status shouldBe Status.INVISIBLE
                }
            }
        }

        When("bookstore report") {
            every { accountService.checkAccountUser(accountId) } returns Unit
            every { reportBookstoreRepository.save(any()) } returns reportBookstore

            val saveReportBookstore = bookstoreService.createReportBookstore(reportBookstoreRequest)

            Then("it should report bookstore") {
                saveReportBookstore.title shouldBe reportBookstoreRequest.title
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