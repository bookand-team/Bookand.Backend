package kr.co.bookand.backend.bookstore.domain.dto

import kr.co.bookand.backend.article.domain.dto.KotlinArticleResponse
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.bookstore.domain.KotlinBookstoreType
import kr.co.bookand.backend.bookstore.domain.KotlinReportBookstore
import kr.co.bookand.backend.bookstore.domain.dto.BookStoreDto.BookStoreAddressResponse
import kr.co.bookand.backend.common.KotlinPageResponse
import kr.co.bookand.backend.common.domain.Status
import org.springframework.data.domain.Page
import java.time.LocalDateTime

data class KotlinBookstoreIdResponse(
    val id: Long
)

data class KotlinBookstoreRequest(
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

data class KotlinBookstoreListRequest(
    val bookstoreList: List<Long>
)

data class KotlinReportBookstoreRequest(
    val name: String,
    val address: String
)

data class KotlinAnswerReportRequest(
    val answerTitle: String,
    val answerContent: String
)

data class KotlinBookstoreResponse(
    val id: Long,
    val name: String,
    val info: BookStoreInfo,
    val mainImage: String,
    val themeList: MutableList<KotlinBookstoreType>,
    val subImage: List<KotlinBookstoreImageResponse>,
    val status: Status,
    val view: Int,
    val isBookmark: Boolean,
    val createdDate: String,
    val modifiedDate: String,
    val displayDate: LocalDateTime?,
    val articleResponse: List<KotlinArticleResponse>,
    val visibility: Boolean
) {
    constructor(kotlinBookstore: KotlinBookstore) : this(
        id = kotlinBookstore.id,
        name = kotlinBookstore.name,
        info = BookStoreInfo(
            address = kotlinBookstore.address,
            businessHours = kotlinBookstore.businessHours,
            contact = kotlinBookstore.contact,
            facility = kotlinBookstore.facility,
            latitude = kotlinBookstore.latitude,
            longitude = kotlinBookstore.longitude,
            sns = kotlinBookstore.sns
        ),
        mainImage = kotlinBookstore.mainImage,
        themeList = kotlinBookstore.themeList.map { it.theme }.toMutableList(),
        subImage = kotlinBookstore.imageList.map {
            KotlinBookstoreImageResponse(it.id, it.url, it.bookStore?.name)
        },
        status = kotlinBookstore.status,
        view = kotlinBookstore.view,
        isBookmark = false,
        createdDate = kotlinBookstore.createdAt.toString(),
        modifiedDate = kotlinBookstore.modifiedAt.toString(),
        displayDate = kotlinBookstore.displayedAt,
        articleResponse = kotlinBookstore.introducedBookstoreList.map {
            KotlinArticleResponse(it.article)
        },
        visibility = kotlinBookstore.visibility
    )

}

data class KotlinWebBookstoreResponse(
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
    constructor(bookstore: KotlinBookstore) : this(
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

data class KotlinBookstoreSimpleResponse(
    val id: Long,
    val name: String,
    val introduction: String,
    val mainImage: String,
    val themeList: List<String>,
    val isBookmark: Boolean
) {
    constructor(bookstore: KotlinBookstore, isBookmark: Boolean) : this(
        id = bookstore.id,
        name = bookstore.name,
        introduction = bookstore.introduction,
        mainImage = bookstore.mainImage,
        themeList = bookstore.themeList.map { it.theme.name },
        isBookmark = isBookmark
    )
}

data class BookStoreInfo(
    val address: String,
    val businessHours: String,
    val contact: String,
    val facility: String,
    val latitude: String,
    val longitude: String,
    val sns: String
)

data class KotlinBookstoreImageResponse(
    val id: Long,
    val url: String,
    val bookstore: String?
)

data class KotlinBookstorePageResponse(
    val data: KotlinPageResponse<KotlinBookstoreSimpleResponse>
) {
    companion object {
        fun of(pageResponse: Page<KotlinBookstoreSimpleResponse>): KotlinBookstorePageResponse {
            return KotlinBookstorePageResponse(
                data = KotlinPageResponse.of(pageResponse)
            )
        }
    }
}

data class KotlinWebBookstorePageResponse(
    val article: KotlinPageResponse<KotlinWebBookstoreResponse>
) {
    companion object {
        fun of(pageResponse: Page<KotlinWebBookstoreResponse>): KotlinWebBookstorePageResponse {
            return KotlinWebBookstorePageResponse(
                article = KotlinPageResponse.of(pageResponse)
            )
        }
    }
}

data class KotlinReportBookstoreIdResponse(
    val id: Long
)

data class KotlinReportBookstoreListResponse(
    val data: KotlinPageResponse<KotlinReportBookstoreResponse>
) {
    companion object {
        fun of(pageResponse: Page<KotlinReportBookstoreResponse>): KotlinReportBookstoreListResponse {
            return KotlinReportBookstoreListResponse(
                data = KotlinPageResponse.of(pageResponse)
            )
        }
    }
}

data class KotlinReportBookstoreResponse(
    val reportId: Long,
    val providerEmail: String?,
    val bookstoreName: String,
    val reportCount: Int,
    val isAnswered: Boolean,
    val createdAt: String,
    val answeredAt: String
) {
    constructor(reportBookstore: KotlinReportBookstore) : this(
        reportId = reportBookstore.id,
        providerEmail = reportBookstore.account?.providerEmail,
        bookstoreName = reportBookstore.name,
        reportCount = 1,
        isAnswered = reportBookstore.isAnswered,
        createdAt = reportBookstore.createdAt.toString(),
        answeredAt = reportBookstore.answeredAt
    )
}

data class KotlinBookstoreAddressResponse(
    val id: Long,
    val name: String,
    val mainImage: String,
    val theme: List<KotlinBookstoreType>,
    val latitude: String,
    val longitude: String,
    val address: String,
    val isBookmark: Boolean
){
    constructor(bookstore: KotlinBookstore, isBookmark: Boolean) : this(
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

data class KotlinBookStoreAddressListResponse(
    val bookStoreAddressListResponse: List<KotlinBookstoreAddressResponse>
)