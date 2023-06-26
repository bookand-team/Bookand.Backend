package kr.co.bookand.backend.article.dto

import kr.co.bookand.backend.article.model.Article
import kr.co.bookand.backend.article.model.ArticleCategory
import kr.co.bookand.backend.bookstore.model.Bookstore
import kr.co.bookand.backend.bookstore.dto.BookstoreSimpleResponse
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.MemberIdFilter
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.common.Status
import org.springframework.data.domain.Page
import java.time.LocalDateTime

data class ArticleRequest(
    val title: String,
    val subTitle: String,
    val mainImage: String,
    val content: String,
    val category: String,
    val writer: String,
    val articleTagList: List<String>,
    val bookstoreList: List<Long>,
    val articleFilter: ArticleFilter
)

data class IntroducedBookstoreRequest(
    val article: Article,
    val bookstore: Bookstore
)

data class ArticleIdResponse(
    val id: Long
)

data class ArticleDetailResponse(
    val id: Long,
    val title: String,
    val subTitle: String,
    val mainImage: String,
    val content: String,
    val category: ArticleCategory,
    val writer: String,
    val status: Status,
    val view: Int,
    val bookmark: Boolean,
    val visibility: Boolean,
    val articleTagList: List<String>,
    val bookStoreList: List<BookstoreSimpleResponse>,
    val filter: ArticleFilter,
    val createdDate: String,
    val modifiedDate: String
)

data class ArticleWebResponse(
    val id: Long,
    val title: String,
    val subTitle: String,
    val mainImage: String,
    val content: String,
    val category: ArticleCategory,
    val writer: String,
    val status: Status,
    val view: Int,
    val articleTagList: List<String>,
    val visibility: Boolean,
    val createdDate: String,
    val modifiedDate: String
) {
    constructor(article: Article) : this(
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
        modifiedDate = article.modifiedAt.toString()
    )

}

data class ArticleFilter(
    val deviceOS: DeviceOSFilter,
    val memberId: MemberIdFilter
)

data class ArticlePageResponse(
    val data: PageResponse<ArticleResponse>
) {
    companion object {
        fun of(pageResponse: Page<ArticleResponse>, totalElements: Long): ArticlePageResponse {
            return ArticlePageResponse(
                data = PageResponse.ofCursor(pageResponse, totalElements)
            )
        }
    }
}

data class ArticleWebPageResponse(
    val article: PageResponse<ArticleWebResponse>
) {
    companion object {
        fun of(pageResponse: Page<ArticleWebResponse>): ArticleWebPageResponse {
            return ArticleWebPageResponse(
                article = PageResponse.of(pageResponse)
            )
        }
    }
}

data class ArticleListRequest(
    val articleIdList : List<Long>
)
data class ArticleResponse(
    val id: Long,
    val title: String,
    val subTitle: String,
    val mainImage: String,
    val category: ArticleCategory,
    val writer: String,
    val status: Status,
    val view: Int,
    val isBookmark: Boolean,
    val articleTagList: List<String>,
    val visibility: Boolean,
    val createdDate: String,
    val modifiedDate: String
){
    constructor(article : Article): this(
        id = article.id,
        title = article.title,
        subTitle = article.subTitle,
        mainImage = article.mainImage,
        category = article.category,
        writer = article.writer,
        status = article.status,
        view = article.viewCount,
        isBookmark = false,
        articleTagList = article.articleTagList.map { it.tag },
        visibility = article.visibility,
        createdDate = article.createdAt.toString(),
        modifiedDate = article.modifiedAt.toString()
    )
}