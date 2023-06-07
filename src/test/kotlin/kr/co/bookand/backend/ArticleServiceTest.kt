package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.service.KotlinAccountService
import kr.co.bookand.backend.article.domain.ArticleCategory
import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.article.domain.KotlinArticleTag
import kr.co.bookand.backend.article.domain.KotlinIntroducedBookstore
import kr.co.bookand.backend.article.domain.dto.KotlinArticleRequest
import kr.co.bookand.backend.article.domain.dto.KotlinIntroducedBookstoreRequest
import kr.co.bookand.backend.article.repository.KotlinArticleRepository
import kr.co.bookand.backend.article.repository.KotlinArticleTagRepository
import kr.co.bookand.backend.article.repository.KotlinIntroducedBookstoreRepository
import kr.co.bookand.backend.article.service.KotlinArticleService
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreRepository
import kr.co.bookand.backend.common.domain.DeviceOSFilter
import kr.co.bookand.backend.common.domain.MemberIdFilter
import kr.co.bookand.backend.common.domain.Status
import java.time.LocalDateTime
import java.util.*

class ArticleServiceTest : BehaviorSpec({
    val articleRepository = mockk<KotlinArticleRepository>()
    val accountService = mockk<KotlinAccountService>()
    val bookstoreRepository = mockk<KotlinBookstoreRepository>()
    val introducedBookstoreRepository = mockk<KotlinIntroducedBookstoreRepository>()
    val articleTagRepository = mockk<KotlinArticleTagRepository>()

    val articleService = KotlinArticleService(
        articleRepository,
        accountService,
        bookstoreRepository,
        introducedBookstoreRepository,
        articleTagRepository,
    )

    Given("article test") {
        val accountId = 1L
        val articleId = 1L

        val article = KotlinArticle(
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

        val bookstore = KotlinBookstore(
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

        val bookstore2 = KotlinBookstore(
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

        val articleTag1 = KotlinArticleTag(
            id = 1L,
            article = article,
            tag = "tag1"
        )

        val articleTag2 = KotlinArticleTag(
            id = 2L,
            article = article,
            tag = "tag2"
        )

        val articleRequest = KotlinArticleRequest(
            title = "title",
            subTitle = "subTitle",
            content = "content",
            mainImage = "mainImage",
            category = ArticleCategory.BOOKSTORE_REVIEW.toString(),
            writer = "writer",
            bookStoreList = listOf(1L, 2L),
            tagList = listOf("tag1", "tag2")
        )

        val updateArticleRequest = KotlinArticleRequest(
            title = "title2",
            subTitle = "subTitle2",
            content = "content2",
            mainImage = "mainImage2",
            category = ArticleCategory.BOOKSTORE_REVIEW.toString(),
            writer = "writer",
            bookStoreList = listOf(1L, 2L),
            tagList = listOf("tag1", "tag2")
        )

        val introducedBookstore = KotlinIntroducedBookstore(
            KotlinIntroducedBookstoreRequest(
                article = article,
                bookstore = bookstore
            )
        )

        val introducedBookstore2 = KotlinIntroducedBookstore(
            KotlinIntroducedBookstoreRequest(
                article = article,
                bookstore = bookstore2
            )
        )



        When("create article") {

            When("account is not Admin") {
                every { accountService.checkAccountAdmin(2L) } throws RuntimeException("Not admin")

                val exception = shouldThrow<RuntimeException> {
                    articleService.createArticle(2L, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe "Not admin"
                }
            }

            When("duplicate title") {
                every { accountService.checkAccountAdmin(1L) } returns Unit
                every { articleRepository.existsByTitle("title") } returns true

                val exception = shouldThrow<RuntimeException> {
                    articleService.createArticle(1L, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe "DUPLICATE ARTICLE"
                }
            }

            When("not found bookstore") {
                every { accountService.checkAccountAdmin(accountId) } returns Unit
                every { articleRepository.existsByTitle("title") } returns false
                every { articleRepository.save(any()) } returns article
                every { bookstoreRepository.findById(any()) } returns Optional.ofNullable(null)

                val exception = shouldThrow<RuntimeException> {
                    articleService.createArticle(accountId, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe "NOT FOUND BOOKSTORE"
                }
            }

            When("success create article") {
                every { accountService.checkAccountAdmin(accountId) } returns Unit
                every { articleRepository.existsByTitle("title") } returns false
                every { bookstoreRepository.findById(1L) } returns Optional.of(bookstore)
                every { bookstoreRepository.findById(2L) } returns Optional.of(bookstore2)
                every { articleRepository.save(any()) } returns article
                every { introducedBookstoreRepository.save(any()) } returns introducedBookstore andThen introducedBookstore2
                every { articleTagRepository.save(any()) } returns articleTag1 andThen articleTag2
            }

            articleService.createArticle(accountId, articleRequest)

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
                article.introducedBookstoreList[0].bookStore.id shouldBe bookstore.id
                article.introducedBookstoreList[1].bookStore.id shouldBe bookstore2.id
            }

        }

        When("update article") {

            When("account is not Admin") {
                every { accountService.checkAccountAdmin(2L) } throws RuntimeException("Not admin")

                val exception = shouldThrow<RuntimeException> {
                    articleService.createArticle(2L, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe "Not admin"
                }
            }

            When("not found article") {
                every { accountService.checkAccountAdmin(accountId) } returns Unit
                every { articleRepository.findById(articleId) } returns Optional.ofNullable(null)

                val exception = shouldThrow<RuntimeException> {
                    articleService.updateArticle(accountId, articleId, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe "NOT FOUND ARTICLE"
                }
            }

            When("not found bookstore2") {
                every { accountService.checkAccountAdmin(accountId) } returns Unit
                every { articleRepository.findById(articleId) } returns Optional.of(article)
                every { bookstoreRepository.findById(1L) } returns Optional.empty()
                every { bookstoreRepository.findById(2L) } returns Optional.empty()
                every { introducedBookstoreRepository.delete(any()) } returns Unit

                val exception = shouldThrow<RuntimeException> {
                    articleService.updateArticle(accountId, articleId, articleRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe "NOT FOUND BOOKSTORE"
                }
            }

            When("update article info") {
                every { accountService.checkAccountAdmin(accountId) } returns Unit
                every { articleRepository.findById(articleId) } returns Optional.of(article)
                every { introducedBookstoreRepository.delete(any()) } returns Unit
                every { bookstoreRepository.findById(1L) } returns Optional.of(bookstore)
                every { bookstoreRepository.findById(2L) } returns Optional.of(bookstore2)
                every { introducedBookstoreRepository.save(any()) } returns introducedBookstore andThen introducedBookstore2
                every { articleTagRepository.save(any()) } returns articleTag1 andThen articleTag2
                every { articleTagRepository.delete(any()) } returns Unit
                every { articleRepository.save(any()) } returns article

                articleService.updateArticle(accountId, articleId, updateArticleRequest)

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
                    article.introducedBookstoreList[0].bookStore.name shouldBe bookstore.name
                    article.introducedBookstoreList[1].bookStore.name shouldBe bookstore2.name
                }
            }

            When("update article Status") {
                every { accountService.checkAccountAdmin(accountId) } returns Unit
                every { articleRepository.findById(articleId) } returns Optional.of(article)
                every { articleRepository.save(any()) } returns article

                articleService.updateArticleStatus(1L, Status.VISIBLE)

                Then("it should return article") {
                    article.status shouldBe Status.VISIBLE
                }
            }
        }

        When("delete article") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { articleService.getArticle(articleId) } returns article
            every { introducedBookstoreRepository.delete(any()) } returns Unit

            articleService.removeArticle(accountId, articleId)

            Then("it should return article") {
                val deletedArticle = articleService.getArticle(articleId)
                deletedArticle.visibility shouldBe false
            }
        }

        When("delete article list") {
            every { accountService.checkAccountAdmin(accountId) } returns Unit
            every { articleService.getArticle(articleId) } returns article
            every { introducedBookstoreRepository.delete(any()) } returns Unit

            articleService.removeArticleList(accountId, listOf(articleId))

            Then("it should return article") {
                val deletedArticle = articleService.getArticle(articleId)
                deletedArticle.visibility shouldBe false
            }
        }



    }

})