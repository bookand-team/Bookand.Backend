package kr.co.bookand.backend.util.dummy


import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.article.domain.KotlinArticleCategory
import kr.co.bookand.backend.article.domain.KotlinArticleTag
import kr.co.bookand.backend.article.domain.KotlinIntroducedBookstore
import kr.co.bookand.backend.article.repository.KotlinArticleRepository
import kr.co.bookand.backend.article.repository.KotlinArticleTagRepository
import kr.co.bookand.backend.article.repository.KotlinIntroducedBookstoreRepository
import kr.co.bookand.backend.bookstore.domain.*
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreImageRepository
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreRepository
import kr.co.bookand.backend.bookstore.repository.KotlinBookstoreThemeRepository
import kr.co.bookand.backend.common.KotlinDeviceOSFilter
import kr.co.bookand.backend.common.KotlinMemberIdFilter
import kr.co.bookand.backend.common.KotlinStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Component
class KotlinArticleAndBookstoreDummyData(
    private val articleRepository: KotlinArticleRepository,
    private val articleTagRepository: KotlinArticleTagRepository,
    private val bookstoreRepository: KotlinBookstoreRepository,
    private val bookstoreImageRepository: KotlinBookstoreImageRepository,
    private val bookstoreThemeRepository: KotlinBookstoreThemeRepository,
    private val introducedBookstoreRepository: KotlinIntroducedBookstoreRepository
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
            val kotlinBookstore = KotlinBookstore(
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
                status = KotlinStatus.VISIBLE,
                view = 0,
                bookmark = 0,
                displayedAt = null
            )
            val saveBookstore = bookstoreRepository.save(kotlinBookstore)
            val bookstoreImageDummyData = bookstoreImageDummyData(saveBookstore)
            val bookstoreThemeDummyData = bookstoreThemeDummyData(saveBookstore)
            kotlinBookstore.updateBookstoreImageList(bookstoreImageDummyData)
            kotlinBookstore.updateBookstoreThemeList(bookstoreThemeDummyData)
        }

    }

    fun dummyArticle() {

        if (articleRepository.count() > 0) {
            log.info("[0] 게시글이 이미 존재하여 더미를 생성하지 않았습니다.")
            return
        }

        for (i in 1..15) {
            val article = KotlinArticle(
                title = "title $i",
                subTitle = "subTitle $i",
                content = "content $i",
                mainImage = "https://picsum.photos/700/700",
                writer = "writer $i",
                viewCount = 0,
                displayedAt = null,
                category = KotlinArticleCategory.BOOKSTORE_REVIEW,
                status = KotlinStatus.VISIBLE,
                deviceOSFilter = KotlinDeviceOSFilter.ALL,
                memberIdFilter = KotlinMemberIdFilter.ALL
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
            val introducedBookstore = KotlinIntroducedBookstore(
                bookstore = bookstoreRepository.findById(i.toLong()).get(),
                article = articleRepository.findById(i.toLong()).get()
            )
            introducedBookstoreRepository.save(introducedBookstore)
        }
    }

    private fun articleTagDummyData(article: KotlinArticle): MutableList<KotlinArticleTag> {
        val articleTagList: MutableList<KotlinArticleTag> = ArrayList()
        for (i in 0..2) {
            val articleTag = KotlinArticleTag(
                tag = "name $i",
                article = article
            )
            articleTagRepository.save(articleTag)
            articleTagList.add(articleTag)
        }
        return articleTagList
    }

    private fun bookstoreImageDummyData(bookstore: KotlinBookstore): MutableList<KotlinBookstoreImage> {
        val bookstoreImageList: MutableList<KotlinBookstoreImage> = ArrayList()
        for (i in 0..2) {

            val bookstoreImage = KotlinBookstoreImage(
                url = "https://picsum.photos/700/700",
                bookstore = bookstore
            )
            bookstoreImageRepository.save(bookstoreImage)
            bookstoreImageList.add(bookstoreImage)
        }
        return bookstoreImageList
    }

    private fun bookstoreThemeDummyData(bookstore: KotlinBookstore): MutableList<KotlinBookstoreTheme> {
        val bookstoreThemeList: MutableList<KotlinBookstoreTheme> = ArrayList()
        val bookstoreTypes = KotlinBookstoreType.randomEnum()
        for (i in 0..2) {
            val bookStoreTheme = KotlinBookstoreTheme(
                theme = bookstoreTypes[i],
                bookstore = bookstore
            )
            bookstoreThemeRepository.save(bookStoreTheme)
            bookstoreThemeList.add(bookStoreTheme)
        }
        return bookstoreThemeList
    }

}