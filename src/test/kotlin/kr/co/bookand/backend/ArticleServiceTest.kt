package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.model.Account
import kr.co.bookand.backend.account.model.AccountStatus
import kr.co.bookand.backend.account.model.Role
import kr.co.bookand.backend.article.model.*
import kr.co.bookand.backend.article.dto.ArticleListRequest
import kr.co.bookand.backend.article.dto.ArticleRequest
import kr.co.bookand.backend.article.dto.IntroducedBookstoreRequest
import kr.co.bookand.backend.article.repository.ArticleRepository
import kr.co.bookand.backend.article.repository.ArticleTagRepository
import kr.co.bookand.backend.article.repository.IntroducedBookstoreRepository
import kr.co.bookand.backend.article.service.ArticleService
import kr.co.bookand.backend.bookmark.service.BookmarkService
import kr.co.bookand.backend.bookstore.model.Bookstore
import kr.co.bookand.backend.bookstore.repository.BookstoreRepository
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.MemberIdFilter
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.common.exception.BookandException
import java.time.LocalDateTime
import java.util.*

class ArticleServiceTest : BehaviorSpec({
    val articleRepository = mockk<ArticleRepository>()
    val bookstoreRepository = mockk<BookstoreRepository>()
    val introducedBookstoreRepository = mockk<IntroducedBookstoreRepository>()
    val articleTagRepository = mockk<ArticleTagRepository>()
    val bookmarkService = mockk<BookmarkService>()

    val articleService = ArticleService(
        articleRepository,
        bookstoreRepository,
        introducedBookstoreRepository,
        articleTagRepository,
        bookmarkService
    )

    Given("article test") {
        val articleId = 1L

        val account = Account(
            1L,
            "email@email.com",
            "password",
            "nickname",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Role.USER,
            AccountStatus.NORMAL
        )

        val adminAccount = Account(
            2L,
            "admin@email.com",
            "password",
            "admin",
            "provider",
            "providerEmail",
            "profileImage",
            LocalDateTime.now(),
            LocalDateTime.now(),
            Role.ADMIN,
            AccountStatus.NORMAL
        )

        val article = Article(
            id = articleId,
            title = "title",
            subTitle = "subTitle",
            content = "content",
            mainImage = "mainImage",
            category = ArticleCategory.BOOKSTORE_REVIEW,
            writer = "writer",
            viewCount = 0,
            displayedAt = LocalDateTime.now(),
            status = Status.VISIBLE,
            deviceOSFilter = DeviceOSFilter.ALL,
            memberIdFilter = MemberIdFilter.ALL,
            articleTagList = mutableListOf(),
            introducedBookstoreList = mutableListOf(),
        )

        val bookstore = Bookstore(
            id = 1L,
            name = "name",
            address = "address",
            businessHours = "businessHours",
            contact = "contact",
            facility = "facility",
            sns = "sns",
            latitude = "latitude",
            longitude = "longitude",
            introduction = "introduction",
            mainImage = "mainImage",
            status = Status.VISIBLE,
            view = 0,
            bookmark = 0,
            displayedAt = LocalDateTime.now(),
            themeList = mutableListOf(),
            imageList = mutableListOf()
        )

        val bookstore2 = Bookstore(
            id = 2L,
            name = "name2",
            address = "address2",
            businessHours = "businessHours",
            contact = "contact",
            facility = "facility",
            sns = "sns",
            latitude = "latitude",
            longitude = "longitude",
            introduction = "introduction",
            mainImage = "mainImage",
            status = Status.VISIBLE,
            view = 0,
            bookmark = 0,
            displayedAt = LocalDateTime.now(),
            themeList = mutableListOf(),
            imageList = mutableListOf()
        )

        val articleTag1 = ArticleTag(
            id = 1L,
            article = article,
            tag = "tag1"
        )

        val articleTag2 = ArticleTag(
            id = 2L,
            article = article,
            tag = "tag2"
        )

        val articleRequest = ArticleRequest(
            title = "title",
            subTitle = "subTitle",
            content = "content",
            mainImage = "mainImage",
            category = ArticleCategory.BOOKSTORE_REVIEW.toString(),
            writer = "writer",
            bookStoreList = listOf(1L, 2L),
            tagList = listOf("tag1", "tag2")
        )

        val updateArticleRequest = ArticleRequest(
            title = "title2",
            subTitle = "subTitle2",
            content = "content2",
            mainImage = "mainImage2",
            category = ArticleCategory.BOOKSTORE_REVIEW.toString(),
            writer = "writer",
            bookStoreList = listOf(1L, 2L),
            tagList = listOf("tag1", "tag2")
        )

        val introducedBookstore = IntroducedBookstore(
            IntroducedBookstoreRequest(
                article = article,
                bookstore = bookstore
            )
        )

        val introducedBookstore2 = IntroducedBookstore(
            IntroducedBookstoreRequest(
                article = article,
                bookstore = bookstore2
            )
        )

        val articleIdList = ArticleListRequest(
            articleIdList = listOf(1L, 2L)
        )



        When("create article") {

            When("account is not Admin") {

                val exception = shouldThrow<BookandException> {
                    articleService.createArticle(account, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe ErrorCode.ROLE_ACCESS_ERROR.errorLog
                }
            }

            When("duplicate title") {
                every { articleRepository.existsByTitle("title") } returns true

                val exception = shouldThrow<BookandException> {
                    articleService.createArticle(adminAccount, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe ErrorCode.DUPLICATE_ARTICLE.errorLog
                }
            }

            When("not found bookstore") {
                every { articleRepository.existsByTitle("title") } returns false
                every { articleRepository.save(any()) } returns article
                every { bookstoreRepository.findById(any()) } returns Optional.ofNullable(null)

                val exception = shouldThrow<BookandException> {
                    articleService.createArticle(adminAccount, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe ErrorCode.NOT_FOUND_BOOKSTORE.errorLog
                }
            }

            When("success create article") {
                every { articleRepository.existsByTitle("title") } returns false
                every { bookstoreRepository.findById(1L) } returns Optional.of(bookstore)
                every { bookstoreRepository.findById(2L) } returns Optional.of(bookstore2)
                every { articleRepository.save(any()) } returns article
                every { introducedBookstoreRepository.save(any()) } returns introducedBookstore andThen introducedBookstore2
                every { articleTagRepository.save(any()) } returns articleTag1 andThen articleTag2
            }

            articleService.createArticle(adminAccount, articleRequest)

            Then("it should return article") {
                article.title shouldBe "title"
                article.subTitle shouldBe "subTitle"
                article.content shouldBe "content"
                article.mainImage shouldBe "mainImage"
                article.category shouldBe ArticleCategory.BOOKSTORE_REVIEW
                article.writer shouldBe "writer"
                article.viewCount shouldBe 0
                article.status shouldBe Status.VISIBLE
                article.deviceOSFilter shouldBe DeviceOSFilter.ALL
                article.memberIdFilter shouldBe MemberIdFilter.ALL
                article.articleTagList[0].tag shouldBe articleTag1.tag
                article.articleTagList[1].tag shouldBe articleTag2.tag
                article.introducedBookstoreList[0].bookstore.id shouldBe bookstore.id
                article.introducedBookstoreList[1].bookstore.id shouldBe bookstore2.id
            }

        }

        When("update article") {

            When("account is not Admin") {

                val exception = shouldThrow<BookandException> {
                    articleService.createArticle(account, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe ErrorCode.ROLE_ACCESS_ERROR.errorLog
                }
            }

            When("not found article") {
                every { articleRepository.findById(articleId) } returns Optional.ofNullable(null)

                val exception = shouldThrow<RuntimeException> {
                    articleService.updateArticle(adminAccount, articleId, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe ErrorCode.NOT_FOUND_ARTICLE.errorLog
                }
            }

            When("not found bookstore2") {
                every { articleRepository.findById(articleId) } returns Optional.of(article)
                every { bookstoreRepository.findById(1L) } returns Optional.empty()
                every { bookstoreRepository.findById(2L) } returns Optional.empty()
                every { introducedBookstoreRepository.delete(any()) } returns Unit

                val exception = shouldThrow<RuntimeException> {
                    articleService.updateArticle(adminAccount, articleId, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe ErrorCode.NOT_FOUND_BOOKSTORE.errorLog
                }
            }

            When("update article info") {
                every { articleRepository.findById(articleId) } returns Optional.of(article)
                every { introducedBookstoreRepository.delete(any()) } returns Unit
                every { bookstoreRepository.findById(1L) } returns Optional.of(bookstore)
                every { bookstoreRepository.findById(2L) } returns Optional.of(bookstore2)
                every { introducedBookstoreRepository.save(any()) } returns introducedBookstore andThen introducedBookstore2
                every { articleTagRepository.save(any()) } returns articleTag1 andThen articleTag2
                every { articleTagRepository.delete(any()) } returns Unit
                every { articleRepository.save(any()) } returns article

                articleService.updateArticle(adminAccount, articleId, updateArticleRequest)

                Then("it should return article") {
                    article.title shouldBe "title2"
                    article.subTitle shouldBe "subTitle2"
                    article.content shouldBe "content2"
                    article.mainImage shouldBe "mainImage2"
                    article.category shouldBe ArticleCategory.BOOKSTORE_REVIEW
                    article.writer shouldBe "writer"
                    article.viewCount shouldBe 0
                    article.status shouldBe Status.VISIBLE
                    article.deviceOSFilter shouldBe DeviceOSFilter.ALL
                    article.memberIdFilter shouldBe MemberIdFilter.ALL
                    article.articleTagList[0].tag shouldBe articleTag1.tag
                    article.articleTagList[1].tag shouldBe articleTag2.tag
                    article.introducedBookstoreList[0].bookstore.name shouldBe bookstore.name
                    article.introducedBookstoreList[1].bookstore.name shouldBe bookstore2.name
                }
            }

            When("update article Status") {
                every { articleRepository.findById(articleId) } returns Optional.of(article)
                every { articleRepository.save(any()) } returns article

                articleService.updateArticleStatus(articleId)

                Then("it should return article") {
                    article.status shouldBe Status.INVISIBLE
                }
            }
        }

        When("delete article") {
            every { articleService.getArticle(articleId) } returns article
            every { introducedBookstoreRepository.delete(any()) } returns Unit

            articleService.removeArticle(adminAccount, articleId)

            Then("it should return article") {
                val deletedArticle = articleService.getArticle(articleId)
                deletedArticle.visibility shouldBe false
            }
        }

        When("delete article list") {
            every { articleService.getArticle(articleId) } returns article
            every { articleRepository.findById(any()) } returns Optional.of(article)
            every { introducedBookstoreRepository.delete(any()) } returns Unit

            articleService.removeArticleList(adminAccount, articleIdList)

            Then("it should return article") {
                val deletedArticle = articleService.getArticle(articleId)
                deletedArticle.visibility shouldBe false
            }
        }



    }

})