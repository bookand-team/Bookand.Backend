package kr.co.bookand.backend.util.dummy


import kr.co.bookand.backend.article.model.Article
import kr.co.bookand.backend.article.model.ArticleCategory
import kr.co.bookand.backend.article.model.ArticleTag
import kr.co.bookand.backend.article.model.IntroducedBookstore
import kr.co.bookand.backend.article.repository.ArticleRepository
import kr.co.bookand.backend.article.repository.ArticleTagRepository
import kr.co.bookand.backend.article.repository.IntroducedBookstoreRepository
import kr.co.bookand.backend.bookstore.model.*
import kr.co.bookand.backend.bookstore.repository.BookstoreImageRepository
import kr.co.bookand.backend.bookstore.repository.BookstoreRepository
import kr.co.bookand.backend.bookstore.repository.BookstoreThemeRepository
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.MemberIdFilter
import kr.co.bookand.backend.common.Status
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Component
class ArticleAndBookstoreDummyData(
    private val articleRepository: ArticleRepository,
    private val articleTagRepository: ArticleTagRepository,
    private val bookstoreRepository: BookstoreRepository,
    private val bookstoreImageRepository: BookstoreImageRepository,
    private val bookstoreThemeRepository: BookstoreThemeRepository,
    private val introducedBookstoreRepository: IntroducedBookstoreRepository
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PostConstruct
    @Transactional
    fun dummyData() {
        dummyBookstore()
        dummyArticle()
        dummyIntroducedBookstore()
    }

    private fun dummyBookstore() {
        if (bookstoreRepository.count() > 0) {
            log.info("[0] 서점이 이미 존재하여 더미를 생성하지 않았습니다.")
            return
        }

        for (i in 1..15) {
            val bookstore = Bookstore(
                name = "서점 $i",
                address = "주소 $i",
                businessHours = "영업시간 $i",
                contact = "연락처 $i",
                facility = "시설 $i",
                sns = "sns $i",
                latitude = "127.1051209",
                longitude = "37.3586926",
                mainImage = "https://picsum.photos/700/700",
                introduction = "introduction introduction introduction $i",
                status = Status.VISIBLE,
                view = 0,
                bookmark = 0,
                displayedAt = null
            )
            val saveBookstore = bookstoreRepository.save(bookstore)
            val bookstoreImageDummyData = bookstoreImageDummyData(saveBookstore)
            val bookstoreThemeDummyData = bookstoreThemeDummyData(saveBookstore)
            bookstore.updateBookstoreImageList(bookstoreImageDummyData)
            bookstore.updateBookstoreThemeList(bookstoreThemeDummyData)
        }

    }

    fun dummyArticle() {

        if (articleRepository.count() > 0) {
            log.info("[0] 게시글이 이미 존재하여 더미를 생성하지 않았습니다.")
            return
        }

        for (i in 1..15) {
            val article = Article(
                title = "title $i",
                subTitle = "subTitle $i",
                content = "content $i",
                mainImage = "https://picsum.photos/700/700",
                writer = "writer $i",
                viewCount = 0,
                displayedAt = null,
                category = ArticleCategory.BOOKSTORE_REVIEW,
                status = Status.VISIBLE,
                deviceOSFilter = DeviceOSFilter.ALL,
                memberIdFilter = MemberIdFilter.ALL
            )
            val saveArticle = articleRepository.save(article)
            val articleTagDummyData = articleTagDummyData(saveArticle)
            article.updateArticleTagList(articleTagDummyData)
        }
    }

    private fun dummyIntroducedBookstore() {

        if (introducedBookstoreRepository.count() > 0) {
            log.info("[0] 소개된 서점이 이미 존재하여 더미를 생성하지 않았습니다.")
            return
        }

        for (i in 1..15) {
            val introducedBookstore = IntroducedBookstore(
                bookstore = bookstoreRepository.findById(i.toLong()).get(),
                article = articleRepository.findById(i.toLong()).get()
            )
            introducedBookstoreRepository.save(introducedBookstore)
        }
    }

    private fun articleTagDummyData(article: Article): MutableList<ArticleTag> {
        val articleTagList: MutableList<ArticleTag> = ArrayList()
        for (i in 0..2) {
            val articleTag = ArticleTag(
                tag = "name $i",
                article = article
            )
            articleTagRepository.save(articleTag)
            articleTagList.add(articleTag)
        }
        return articleTagList
    }

    private fun bookstoreImageDummyData(bookstore: Bookstore): MutableList<BookstoreImage> {
        val bookstoreImageList: MutableList<BookstoreImage> = ArrayList()
        for (i in 0..2) {

            val bookstoreImage = BookstoreImage(
                url = "https://picsum.photos/700/700",
                bookstore = bookstore
            )
            bookstoreImageRepository.save(bookstoreImage)
            bookstoreImageList.add(bookstoreImage)
        }
        return bookstoreImageList
    }

    private fun bookstoreThemeDummyData(bookstore: Bookstore): MutableList<BookstoreTheme> {
        val bookstoreThemeList: MutableList<BookstoreTheme> = ArrayList()
        val bookstoreTypes = BookstoreType.randomEnum()
        for (i in 0..2) {
            val bookStoreTheme = BookstoreTheme(
                theme = bookstoreTypes[i],
                bookstore = bookstore
            )
            bookstoreThemeRepository.save(bookStoreTheme)
            bookstoreThemeList.add(bookStoreTheme)
        }
        return bookstoreThemeList
    }

}