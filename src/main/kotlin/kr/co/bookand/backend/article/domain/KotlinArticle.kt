package kr.co.bookand.backend.article.domain

import kr.co.bookand.backend.article.domain.dto.KotlinArticleRequest
import kr.co.bookand.backend.common.domain.DeviceOSFilter
import kr.co.bookand.backend.common.domain.KotlinBaseEntity
import kr.co.bookand.backend.common.domain.MemberIdFilter
import kr.co.bookand.backend.common.domain.Status
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class KotlinArticle(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "karticle_id")
    var id: Long = 0,

    var title: String,
    var subTitle: String,
    var content: String,
    var mainImage: String,
    var writer: String,
    @Enumerated(EnumType.STRING)
    var category: KotlinArticleCategory,

    var viewCount: Int,
    var displayedAt: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    var status: Status,
    @Enumerated(EnumType.STRING)
    var deviceOSFilter: DeviceOSFilter,
    @Enumerated(EnumType.STRING)
    val memberIdFilter: MemberIdFilter,

    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    var articleTagList: MutableList<KotlinArticleTag> = mutableListOf(),

    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    var introducedBookstoreList: MutableList<KotlinIntroducedBookstore> = mutableListOf()

) : KotlinBaseEntity() {

    constructor(kotlinArticleRequest : KotlinArticleRequest) : this(
        title = kotlinArticleRequest.title,
        subTitle = kotlinArticleRequest.subTitle,
        content = kotlinArticleRequest.content,
        mainImage = kotlinArticleRequest.mainImage,
        writer = kotlinArticleRequest.writer,
        viewCount = 0,
        displayedAt = null,
        category = KotlinArticleCategory.valueOf(kotlinArticleRequest.category),
        status = Status.VISIBLE,
        deviceOSFilter = DeviceOSFilter.ALL,
        memberIdFilter = MemberIdFilter.ALL
    )
    fun updateIntroducedBookstore(kotlinIntroducedBookstore: KotlinIntroducedBookstore) {
        introducedBookstoreList.add(kotlinIntroducedBookstore)
    }

    fun updateArticleTag(articleTag: KotlinArticleTag) {
        articleTagList.add(articleTag)
    }
    fun updateArticleStatus(status: Status) {
        this.status = status
    }

    fun removeIntroducedBookstore(kotlinIntroducedBookstore: KotlinIntroducedBookstore) {
        introducedBookstoreList.remove(kotlinIntroducedBookstore)
    }

    fun removeArticleTag(articleTag: KotlinArticleTag) {
        articleTagList.remove(articleTag)
    }

    fun updateArticleData(kotlinArticleRequest: KotlinArticleRequest) {
        this.title = kotlinArticleRequest.title
        this.subTitle = kotlinArticleRequest.subTitle
        this.content = kotlinArticleRequest.content
        this.mainImage = kotlinArticleRequest.mainImage
        this.writer = kotlinArticleRequest.writer
        this.category = KotlinArticleCategory.valueOf(kotlinArticleRequest.category)
    }
}