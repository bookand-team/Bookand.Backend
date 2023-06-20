package kr.co.bookand.backend

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.co.bookand.backend.account.domain.*
import kr.co.bookand.backend.account.service.AccountService
import kr.co.bookand.backend.article.domain.Article
import kr.co.bookand.backend.article.domain.ArticleCategory
import kr.co.bookand.backend.article.repository.ArticleRepository
import kr.co.bookand.backend.bookmark.domain.Bookmark
import kr.co.bookand.backend.bookmark.domain.BookmarkType
import kr.co.bookand.backend.bookmark.domain.BookmarkedBookstore
import kr.co.bookand.backend.bookmark.domain.dto.BookmarkContentListRequest
import kr.co.bookand.backend.bookmark.domain.dto.BookmarkFolderNameRequest
import kr.co.bookand.backend.bookmark.domain.dto.BookmarkFolderRequest
import kr.co.bookand.backend.bookmark.repository.BookmarkRepository
import kr.co.bookand.backend.bookmark.repository.BookmarkedArticleRepository
import kr.co.bookand.backend.bookmark.repository.BookmarkedBookstoreRepository
import kr.co.bookand.backend.bookmark.service.BookmarkService
import kr.co.bookand.backend.bookstore.domain.Bookstore
import kr.co.bookand.backend.bookstore.repository.BookstoreRepository
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.MemberIdFilter
import kr.co.bookand.backend.common.Status
import java.time.LocalDateTime
import java.util.*

class BookmarkServiceTest : BehaviorSpec({
    val bookmarkRepository = mockk<BookmarkRepository>()
    val bookmarkedArticleRepository = mockk<BookmarkedArticleRepository>()
    val bookmarkedBookstoreRepository = mockk<BookmarkedBookstoreRepository>()
    val accountService = mockk<AccountService>()
    val bookstoreRepository = mockk<BookstoreRepository>()
    val articleRepository = mockk<ArticleRepository>()

    val bookmarkService = BookmarkService(
        bookmarkRepository,
        bookmarkedArticleRepository,
        bookmarkedBookstoreRepository,
        accountService,
        bookstoreRepository,
        articleRepository
    )


    Given("BookmarkService Test") {

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

        val bookmark = Bookmark(
            id = 1L,
            folderName = "folderName",
            folderImage = "folderImage",
            bookmarkType = BookmarkType.BOOKSTORE,
            account = adminAccount,
            bookmarkedArticleList = mutableListOf(),
            bookmarkedBookstoreList = mutableListOf(),
        )


        val initBookmarkArticle = Bookmark(
            id = 2L,
            folderName = "모아보기",
            folderImage = "folderImage",
            bookmarkType = BookmarkType.ARTICLE,
            account = adminAccount,
            bookmarkedArticleList = mutableListOf(),
            bookmarkedBookstoreList = mutableListOf(),
        )

        val initBookmarkBookstore = Bookmark(
            id = 3L,
            folderName = "모아보기",
            folderImage = "folderImage",
            bookmarkType = BookmarkType.BOOKSTORE,
            account = adminAccount,
            bookmarkedArticleList = mutableListOf(),
            bookmarkedBookstoreList = mutableListOf(),
        )


        val article = Article(
            id = 1L,
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
            businessHours = "businessHours2",
            contact = "contact2",
            facility = "facility2",
            sns = "sns2",
            latitude = "latitude2",
            longitude = "longitude2",
            introduction = "introduction2",
            mainImage = "mainImage2",
            status = Status.VISIBLE,
            view = 0,
            bookmark = 0,
            displayedAt = LocalDateTime.now(),
            themeList = mutableListOf(),
            imageList = mutableListOf()
        )

        val bookmarkedBookstore = BookmarkedBookstore(
            id = 4L,
            bookmark = bookmark,
            bookstore = bookstore,
        )

        val bookmarkFolderRequest = BookmarkFolderRequest(
            folderName = "folderName1",
            bookmarkType = "BOOKSTORE"
        )

        val kotlinBookmarkNameRequest = BookmarkFolderNameRequest(
            folderName = "folderName1"
        )

        val bookmarkContentListRequest = BookmarkContentListRequest(
            bookmarkType = "BOOKSTORE",
            contentIdList = listOf(1L, 2L)
        )


        val bookmark2 = Bookmark(
            id = 2L,
            folderName = "folderName",
            folderImage = "folderImage",
            bookmarkType = BookmarkType.BOOKSTORE,
            account = adminAccount,
            bookmarkedArticleList = mutableListOf(),
            bookmarkedBookstoreList = mutableListOf(bookmarkedBookstore),
        )

        When("create bookmark") {

            When("create Article") {

                When("fail - not found init bookmark") {
                    every {
                        bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(
                            any(),
                            any(),
                            any()
                        )
                    } returns null

                    val exception = shouldThrow<RuntimeException> {
                        bookmarkService.createBookmarkedArticle(adminAccount, articleId = 1L)
                    }

                    Then("it should throw exception") {
                        exception.message shouldBe "NOT FOUND INIT BOOKMARK"
                    }

                }

                When("fail - not found article") {
                    every {
                        bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(
                            any(),
                            any(),
                            any()
                        )
                    } returns initBookmarkArticle
                    every { articleRepository.findById(any()) } returns Optional.empty()

                    val exception = shouldThrow<RuntimeException> {
                        bookmarkService.createBookmarkedArticle(adminAccount, articleId = 1L)
                    }

                    Then("it should throw exception") {
                        exception.message shouldBe "NOT FOUND ARTICLE"
                    }

                }

                When("success - already bookmarked") {
                    every {
                        bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(
                            any(),
                            any(),
                            any()
                        )
                    } returns bookmark
                    every { articleRepository.findById(any()) } returns Optional.of(article)
                    every {
                        bookmarkedArticleRepository.existsByBookmarkIdAndArticleIdAndAccountId(
                            any(),
                            any(),
                            any()
                        )
                    } returns true
                    every { bookmarkedArticleRepository.deleteByArticleIdAndBookmarkId(any(), any()) } returns Unit

                    bookmarkService.createBookmarkedArticle(adminAccount, articleId = 1L)

                    Then("delete bookmarked article") {
                        bookmark.bookmarkedArticleList.size shouldBe 0
                    }
                }

                When("success - not bookmarked") {
                    every {
                        bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(
                            any(),
                            any(),
                            any()
                        )
                    } returns bookmark
                    every { articleRepository.findById(any()) } returns Optional.of(article)
                    every {
                        bookmarkedArticleRepository.existsByBookmarkIdAndArticleIdAndAccountId(
                            any(),
                            any(),
                            any()
                        )
                    } returns false
                    every { accountService.getAccountById(any()) } returns adminAccount

                    bookmarkService.createBookmarkedArticle(adminAccount, articleId = 1L)

                    Then("it should be success") {
                        bookmark.bookmarkedArticleList.size shouldBe 1
                    }
                }

            }

            When("create Bookstore") {

                When("fail - not found init bookmark") {
                    every {
                        bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(
                            any(),
                            any(),
                            any()
                        )
                    } returns null

                    val exception = shouldThrow<RuntimeException> {
                        bookmarkService.createBookmarkedBookstore(adminAccount, bookstoreId = 1L)
                    }

                    Then("it should throw exception") {
                        exception.message shouldBe "NOT FOUND INIT BOOKMARK"
                    }

                }

                When("fail - not found bookstore") {
                    every {
                        bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(
                            any(),
                            any(),
                            any()
                        )
                    } returns initBookmarkBookstore
                    every { bookstoreRepository.findById(any()) } returns Optional.empty()

                    val exception = shouldThrow<RuntimeException> {
                        bookmarkService.createBookmarkedBookstore(adminAccount, bookstoreId = 1L)
                    }

                    Then("it should throw exception") {
                        exception.message shouldBe "NOT FOUND BOOKSTORE"
                    }

                }

                When("success - already bookmarked") {
                    every {
                        bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(
                            any(),
                            any(),
                            any()
                        )
                    } returns bookmark
                    every { bookstoreRepository.findById(any()) } returns Optional.of(bookstore)
                    every {
                        bookmarkedBookstoreRepository.existsByBookmarkIdAndBookstoreIdAndAccountId(
                            any(),
                            any(),
                            any()
                        )
                    } returns true
                    every { bookmarkedBookstoreRepository.deleteByBookstoreIdAndBookmarkId(any(), any()) } returns Unit

                    bookmarkService.createBookmarkedBookstore(adminAccount, bookstoreId = 1L)

                    Then("delete bookmarked bookstore") {
                        bookmark.bookmarkedBookstoreList.size shouldBe 0
                    }
                }

                When("success - not bookmarked") {
                    every {
                        bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(
                            any(),
                            any(),
                            any()
                        )
                    } returns bookmark
                    every { bookstoreRepository.findById(any()) } returns Optional.of(bookstore)
                    every {
                        bookmarkedBookstoreRepository.existsByBookmarkIdAndBookstoreIdAndAccountId(
                            any(),
                            any(),
                            any()
                        )
                    } returns false
                    every { accountService.getAccountById(any()) } returns adminAccount

                    bookmarkService.createBookmarkedBookstore(adminAccount, bookstoreId = 1L)

                    Then("it should be success") {
                        bookmark.bookmarkedBookstoreList.size shouldBe 1
                    }
                }

            }

        }

        When("bookmark Folder") {
            When("create Bookmark Folder") {
                every { accountService.getAccountById(any()) } returns adminAccount
                every { bookmarkRepository.save(any()) } returns bookmark

                val createBookmarkFolder = bookmarkService.createBookmarkFolder(adminAccount, bookmarkFolderRequest)

                Then("it should be success") {
                    createBookmarkFolder.bookmarkId shouldBe 1L
                }
            }

        }

        When("update Bookmark Folder Name") {
            When("fail - check Bookmark Folder Name") {
                every { bookmarkRepository.findByIdAndAccountId(any(), any()) } returns initBookmarkArticle

                val exception = shouldThrow<RuntimeException> {
                    bookmarkService.updateBookmarkFolderName(adminAccount, 1L, kotlinBookmarkNameRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe "NOT CHANGE INIT BOOKMARK"
                }
            }

            When("success - update Bookmark Folder Name") {
                every { bookmarkRepository.findByIdAndAccountId(any(), any()) } returns bookmark
                every { bookmarkRepository.save(any()) } returns bookmark

                val updateBookmarkFolder = bookmarkService.updateBookmarkFolderName(adminAccount, 1L, kotlinBookmarkNameRequest)

                Then("it should be success") {
                    updateBookmarkFolder.bookmarkId shouldBe 1L
                }
            }
        }


        When("update Bookmark Folder Content") {

            When("fail - check Bookmark Folder Name") {
                every { bookmarkedBookstoreRepository.findByBookmarkIdAndBookstoreId(any(), any()) } returns null

                val exception = shouldThrow<RuntimeException> {
                    bookmarkService.updateBookmarkFolder(adminAccount, 1L, bookmarkContentListRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe "NOT FOUND BOOKMARKED BOOKSTORE"
                }
            }

            When("fail - exist Bookmarked Bookstore") {
                every { bookmarkedBookstoreRepository.findByBookmarkIdAndBookstoreId(any(), any()) } returns bookmarkedBookstore
                every { bookmarkedBookstoreRepository.existsByBookmarkIdAndBookstoreId(any(), any()) } returns true

                val exception = shouldThrow<RuntimeException> {
                    bookmarkService.updateBookmarkFolder(adminAccount, 1L, bookmarkContentListRequest)
                }

                Then("it should throw exception") {
                    exception.message shouldBe "ALREADY EXIST BOOKMARKED BOOKSTORE"
                }
            }

            When("success - update Bookmark Folder Content") {
                every {
                    bookmarkedBookstoreRepository.findByBookmarkIdAndBookstoreId(
                        any(),
                        any()
                    )
                } returns bookmarkedBookstore
                every { bookmarkedBookstoreRepository.existsByBookmarkIdAndBookstoreId(any(), any()) } returns false
                every { accountService.getAccountById(any()) } returns adminAccount
                every { bookstoreRepository.findById(any()) } returns Optional.of(bookstore)
                every { bookmarkedBookstoreRepository.save(any()) } returns bookmarkedBookstore
                every { bookmarkRepository.findByIdAndAccountId(any(), any()) } returns bookmark

                bookmarkService.updateBookmarkFolder(adminAccount, 1L, bookmarkContentListRequest)

                Then("it should be success") {
                    bookmarkedBookstore.bookstore shouldBe bookstore
                }
            }
        }



        When("delete bookmark") {
            When("delete Bookmark Folder"){
                When("fail - check Bookmark Folder Name") {
                    every { bookmarkRepository.findByIdAndAccountId(any(), any()) } returns initBookmarkArticle

                    val exception = shouldThrow<RuntimeException> {
                        bookmarkService.deleteBookmarkFolder(adminAccount, 1L)
                    }

                    Then("it should throw exception") {
                        exception.message shouldBe "NOT CHANGE INIT BOOKMARK"
                    }
                }

                When("success - delete Bookmark Folder") {
                    every { bookmarkRepository.findByIdAndAccountId(any(), any()) } returns bookmark2
                    every { bookmarkedBookstoreRepository.delete(bookmarkedBookstore) } returns Unit

                    bookmarkService.deleteBookmarkFolder(adminAccount, 2L)

                    Then("it should be success") {
                        bookmark2.visibility shouldBe false
                    }
                }
            }

            When("delete Bookmark Content"){
                When("fail - check Bookmark Content") {
                    every { bookmarkRepository.findByIdAndAccountId(any(), any()) } returns null

                    val exception = shouldThrow<RuntimeException> {
                        bookmarkService.deleteBookmarkContent(adminAccount, 1L, bookmarkContentListRequest)
                    }

                    Then("it should throw exception") {
                        exception.message shouldBe "NOT FOUND INIT BOOKMARK"
                    }
                }

                When("success - delete Bookmark Content") {
                    every { bookmarkRepository.findByIdAndAccountId(any(), any()) } returns bookmark
                    every { bookmarkedBookstoreRepository.deleteByBookstoreIdAndBookmarkId(any(), any()) } returns Unit
                    every { bookmarkedBookstoreRepository.findByBookmarkIdAndBookstoreId(any(), any()) } returns bookmarkedBookstore

                    bookmarkService.deleteBookmarkContent(adminAccount, 1L, bookmarkContentListRequest)

                    Then("it should be success") {
                        bookmark.bookmarkedBookstoreList.size shouldBe 0
                    }
                }

            }

            When("delete Init Bookmark Content"){
                When("fail - check Bookmark Content") {
                    every { bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(any(), any(), any()) } returns null

                    val exception = shouldThrow<RuntimeException> {
                        bookmarkService.deleteInitBookmarkContent(adminAccount,  bookmarkContentListRequest)
                    }

                    Then("it should throw exception") {
                        exception.message shouldBe "NOT FOUND INIT BOOKMARK"
                    }
                }

                When("success - delete Bookmark Content") {
                    every {
                        bookmarkRepository.findByAccountIdAndFolderNameAndBookmarkType(
                            any(),
                            any(),
                            any()
                        )
                    } returns initBookmarkBookstore

                    every { bookmarkedBookstoreRepository.deleteByBookstoreIdAndBookmarkId(any(), any()) } returns Unit
                    every { bookmarkRepository.findAllByAccountId(any()) } returns List(1) { bookmark }
                    every { bookmarkedBookstoreRepository.deleteByBookstoreIdAndBookmarkId(any(), any()) } returns Unit

                    bookmarkService.deleteInitBookmarkContent(adminAccount, bookmarkContentListRequest)

                    Then("it should be success") {
                        bookmark.bookmarkedBookstoreList.size shouldBe 0
                    }
                }
            }

        }
    }
})
