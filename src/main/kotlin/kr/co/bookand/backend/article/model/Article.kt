package kr.co.bookand.backend.article.model

import kr.co.bookand.backend.article.dto.ArticleRequest
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.MemberIdFilter
import kr.co.bookand.backend.common.Status
import kr.co.bookand.backend.common.model.BaseEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    var id: Long = 0,

    var title: String,
    var subTitle: String,
    var content: String,
    var mainImage: String,
    var writer: String,
    @Enumerated(EnumType.STRING)
    var category: ArticleCategory,

    var viewCount: Int,
    var bookmarkCount: Int,

    @Enumerated(EnumType.STRING)
    var status: Status,
    @Enumerated(EnumType.STRING)
    var deviceOSFilter: DeviceOSFilter,
    @Enumerated(EnumType.STRING)
    val memberIdFilter: MemberIdFilter,

    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    var articleTagList: MutableList<ArticleTag> = mutableListOf(),

    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    var introducedBookstoreList: MutableList<IntroducedBookstore> = mutableListOf()

) : BaseEntity() {

    constructor(articleRequest : ArticleRequest) : this(
        title = articleRequest.title,
        subTitle = articleRequest.subTitle,
        content = articleRequest.content,
        mainImage = articleRequest.mainImage,
        writer = articleRequest.writer,
        viewCount = 0,
        bookmarkCount = 0,
        category = ArticleCategory.valueOf(articleRequest.category),
        status = Status.VISIBLE,
        deviceOSFilter = articleRequest.articleFilter.deviceOS,
        memberIdFilter = articleRequest.articleFilter.memberId,
        articleTagList = mutableListOf(),
        introducedBookstoreList = mutableListOf()
    )
    fun updateIntroducedBookstore(introducedBookstore: IntroducedBookstore) {
        introducedBookstoreList.add(introducedBookstore)
    }

    fun updateArticleTag(articleTag: ArticleTag) {
        articleTagList.add(articleTag)
    }

    fun updateArticleTagList(articleTagList: MutableList<ArticleTag>) {
        this.articleTagList = articleTagList
    }

    fun updateArticleStatus(status: Status) {
        this.status = status
    }

    fun removeIntroducedBookstore(introducedBookstore: IntroducedBookstore) {
        introducedBookstoreList.remove(introducedBookstore)
    }

    fun removeArticleTag(articleTag: ArticleTag) {
        articleTagList.remove(articleTag)
    }

    fun updateArticleData(articleRequest: ArticleRequest) {
        this.title = articleRequest.title
        this.subTitle = articleRequest.subTitle
        this.content = articleRequest.content
        this.mainImage = articleRequest.mainImage
        this.writer = articleRequest.writer
        this.category = ArticleCategory.valueOf(articleRequest.category)
    }
}