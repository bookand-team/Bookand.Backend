package kr.co.bookand.backend.article.domain.dto

import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.article.domain.KotlinArticleCategory
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.bookstore.domain.dto.KotlinBookstoreSimpleResponse
import kr.co.bookand.backend.common.KotlinPageResponse
import kr.co.bookand.backend.common.domain.DeviceOSFilter
import kr.co.bookand.backend.common.domain.MemberIdFilter
import kr.co.bookand.backend.common.domain.Status
import org.springframework.data.domain.Page
import java.time.LocalDateTime

data class KotlinArticleRequest(
    val title: String,
    val subTitle: String,
    val mainImage: String,
    val content: String,
    val category: String,
    val writer: String,
    val tagList: List<String>,
    val bookStoreList: List<Long>,
)

data class KotlinIntroducedBookstoreRequest(
    val article: KotlinArticle,
    val bookstore: KotlinBookstore
)

data class KotlinArticleIdResponse(
    val id: Long
)

data class KotlinArticleDetailResponse(
    val id: Long,
    val title: String,
    val subTitle: String,
    val mainImage: String,
    val content: String,
    val category: KotlinArticleCategory,
    val writer: String,
    val status: Status,
    val view: Int,
    val bookmark: Boolean,
    val visibility: Boolean,
    val articleTagList: List<String>,
    val bookStoreList: List<KotlinBookstoreSimpleResponse>,
    val filter: KotlinArticleFilter,
    val createdDate: String,
    val modifiedDate: String,
    val displayDate: LocalDateTime?
)
data class KotlinWebArticleResponse(
    val id: Long,
    val title: String,
    val subTitle: String,
    val mainImage: String,
    val content: String,
    val category: KotlinArticleCategory,
    val writer: String,
    val status: Status,
    val view: Int,
    val articleTagList: List<String>,
    val visibility: Boolean,
    val createdDate: String,
    val modifiedDate: String,
    val displayDate: LocalDateTime?
) {
    constructor(article: KotlinArticle) : this(
        id = article.id,
        title = article.title,
        subTitle = article.subTitle,
        mainImage = article.mainImage,
        content = article.content,
        category = article.category,
        writer = article.writer,
        status = article.status,
        view = article.viewCount,
        articleTagList = article.articleTagList.map { it.tag },
        visibility = article.visibility,
        createdDate = article.createdAt.toString(),
        modifiedDate = article.modifiedAt.toString(),
        displayDate = article.displayedAt
    )

}

data class KotlinArticleFilter(
    val deviceOS: DeviceOSFilter,
    val memberId: MemberIdFilter
)

data class KotlinArticlePageResponse(
    val data: KotlinPageResponse<KotlinArticleResponse>
) {
    companion object {
        fun of(pageResponse: Page<KotlinArticleResponse>, totalElements: Long): KotlinArticlePageResponse {
            return KotlinArticlePageResponse(
                data = KotlinPageResponse.ofCursor(pageResponse, totalElements)
            )
        }
    }
}

data class KotlinWebArticlePageResponse(
    val article: KotlinPageResponse<KotlinWebArticleResponse>
) {
    companion object {
        fun of(pageResponse: Page<KotlinWebArticleResponse>): KotlinWebArticlePageResponse {
            return KotlinWebArticlePageResponse(
                article = KotlinPageResponse.of(pageResponse)
            )
        }
    }
}

data class KotlinArticleListRequest(
    val articleIdList : List<Long>
)
data class KotlinArticleResponse(
    val id: Long,
    val title: String,
    val subTitle: String,
    val mainImage: String,
    val category: KotlinArticleCategory,
    val writer: String,
    val status: Status,
    val view: Int,
    val isBookmark: Boolean,
    val articleTagList: List<String>,
    val visibility: Boolean,
    val createdDate: String,
    val modifiedDate: String
){
    constructor(kotlinArticle : KotlinArticle): this(
        id = kotlinArticle.id,
        title = kotlinArticle.title,
        subTitle = kotlinArticle.subTitle,
        mainImage = kotlinArticle.mainImage,
        category = kotlinArticle.category,
        writer = kotlinArticle.writer,
        status = kotlinArticle.status,
        view = kotlinArticle.viewCount,
        isBookmark = false,
        articleTagList = kotlinArticle.articleTagList.map { it.tag },
        visibility = kotlinArticle.visibility,
        createdDate = kotlinArticle.createdAt.toString(),
        modifiedDate = kotlinArticle.modifiedAt.toString()
    )
}