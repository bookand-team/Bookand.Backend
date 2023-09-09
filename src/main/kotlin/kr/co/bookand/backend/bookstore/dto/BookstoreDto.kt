package kr.co.bookand.backend.bookstore.dto

import kr.co.bookand.backend.article.dto.ArticleResponse
import kr.co.bookand.backend.bookstore.model.Bookstore
import kr.co.bookand.backend.bookstore.model.BookstoreType
import kr.co.bookand.backend.bookstore.model.ReportBookstore
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.common.Status
import org.springframework.data.domain.Page
import java.time.LocalDateTime

data class BookstoreIdResponse(
    val id: Long
)

data class BookstoreRequest(
    val name: String,
    val address: String,
    val businessHours: String,
    val contact: String,
    val facility: String,
    val sns: String,
    val latitude: String,
    val longitude: String,
    val introduction: String,
    val mainImage: String,
    val themeList: List<String>,
    val subImageList: List<String>
)

data class BookstoreListRequest(
    val bookstoreList: List<Long>
)

data class ReportBookstoreRequest(
    val name: String,
    val address: String
)

data class AnswerReportRequest(
    val answerTitle: String,
    val answerContent: String
)

data class BookstoreResponse(
    val id: Long,
    val name: String,
    val info: BookstoreInfo,
    val introduction: String,
    val mainImage: String,
    val themeList: MutableList<BookstoreType>,
    val subImage: List<BookstoreImageResponse>,
    val status: Status,
    val view: Int,
    val isBookmark: Boolean,
    val createdDate: String,
    val modifiedDate: String,
    val displayDate: LocalDateTime?,
    val articleResponse: List<ArticleResponse>,
    val visibility: Boolean
) {
    constructor(bookstore: Bookstore) : this(
        id = bookstore.id,
        name = bookstore.name,
        info = BookstoreInfo(
            address = bookstore.address,
            businessHours = bookstore.businessHours,
            contact = bookstore.contact,
            facility = bookstore.facility,
            latitude = bookstore.latitude,
            longitude = bookstore.longitude,
            sns = bookstore.sns
        ),
        introduction = bookstore.introduction,
        mainImage = bookstore.mainImage,
        themeList = bookstore.themeList.map { it.theme }.toMutableList(),
        subImage = bookstore.imageList.map {
            BookstoreImageResponse(it.id, it.url, it.bookstore?.name)
        },
        status = bookstore.status,
        view = bookstore.view,
        isBookmark = false,
        createdDate = bookstore.createdAt.toString(),
        modifiedDate = bookstore.modifiedAt.toString(),
        displayDate = bookstore.displayedAt,
        articleResponse = bookstore.introducedBookstoreList.map {
            ArticleResponse(it.article)
        },
        visibility = bookstore.visibility
    )

}

data class BookstoreWebResponse(
    val id: Long,
    val name: String,
    val address: String,
    val businessHours: String,
    val contact: String,
    val facility: String,
    val sns: String,
    val latitude: String,
    val longitude: String,
    val introduction: String,
    val mainImage: String,
    val status: Status,
    val themeList: List<String>,
    val subImageList: List<String>
) {
    constructor(bookstore: Bookstore) : this(
        id = bookstore.id,
        name = bookstore.name,
        address = bookstore.address,
        businessHours = bookstore.businessHours,
        contact = bookstore.contact,
        facility = bookstore.facility,
        sns = bookstore.sns,
        latitude = bookstore.latitude,
        longitude = bookstore.longitude,
        introduction = bookstore.introduction,
        mainImage = bookstore.mainImage,
        status = bookstore.status,
        themeList = bookstore.themeList.map { it.theme.name },
        subImageList = bookstore.imageList.map { it.url }
    )
}

data class BookstoreSimpleResponse(
    val id: Long,
    val name: String,
    val introduction: String,
    val mainImage: String,
    val themeList: List<String>,
    val isBookmark: Boolean
) {
    constructor(bookstore: Bookstore, isBookmark: Boolean) : this(
        id = bookstore.id,
        name = bookstore.name,
        introduction = bookstore.introduction,
        mainImage = bookstore.mainImage,
        themeList = bookstore.themeList.map { it.theme.name },
        isBookmark = isBookmark
    )
}

data class BookstoreInfo(
    val address: String,
    val businessHours: String,
    val contact: String,
    val facility: String,
    val latitude: String,
    val longitude: String,
    val sns: String
)

data class BookstoreImageResponse(
    val id: Long,
    val url: String,
    val bookstore: String?
)

data class BookstorePageResponse(
    val data: PageResponse<BookstoreSimpleResponse>
) {
    companion object {
        fun of(pageResponse: Page<BookstoreSimpleResponse>): BookstorePageResponse {
            return BookstorePageResponse(
                data = PageResponse.of(pageResponse)
            )
        }
    }
}

data class BookstoreWebPageResponse(
    val article: PageResponse<BookstoreWebResponse>
) {
    companion object {
        fun of(pageResponse: Page<BookstoreWebResponse>): BookstoreWebPageResponse {
            return BookstoreWebPageResponse(
                article = PageResponse.of(pageResponse)
            )
        }
    }
}

data class ReportBookstoreIdResponse(
    val id: Long
)

data class ReportBookstoreListResponse(
    val data: PageResponse<ReportBookstoreResponse>
) {
    companion object {
        fun of(pageResponse: Page<ReportBookstoreResponse>): ReportBookstoreListResponse {
            return ReportBookstoreListResponse(
                data = PageResponse.of(pageResponse)
            )
        }
    }
}

data class ReportBookstoreResponse(
    val reportId: Long,
    val providerEmail: String?,
    val bookstoreName: String,
    val reportCount: Int,
    val isAnswered: Boolean,
    val createdAt: String,
    val answeredAt: String
) {
    constructor(reportBookstore: ReportBookstore) : this(
        reportId = reportBookstore.id,
        providerEmail = reportBookstore.account?.providerEmail,
        bookstoreName = reportBookstore.name,
        reportCount = 1,
        isAnswered = reportBookstore.checkAnswered,
        createdAt = reportBookstore.createdAt.toString(),
        answeredAt = reportBookstore.answeredAt
    )
}

data class BookstoreAddressResponse(
    val id: Long,
    val name: String,
    val mainImage: String,
    val theme: List<BookstoreType>,
    val latitude: String,
    val longitude: String,
    val address: String,
    val isBookmark: Boolean
){
    constructor(bookstore: Bookstore, isBookmark: Boolean) : this(
        id = bookstore.id,
        name = bookstore.name,
        mainImage = bookstore.mainImage,
        theme = bookstore.themeList.map { it.theme },
        latitude = bookstore.latitude,
        longitude = bookstore.longitude,
        address = bookstore.address,
        isBookmark = isBookmark
    )
}

data class BookstoreAddressListResponse(
    val bookStoreAddressListResponse: List<BookstoreAddressResponse>
)