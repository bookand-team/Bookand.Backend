package kr.co.bookand.backend.bookmark.domain.dto

import io.swagger.annotations.ApiModelProperty
import kr.co.bookand.backend.article.domain.KotlinArticle
import kr.co.bookand.backend.bookmark.domain.KotlinBookmark
import kr.co.bookand.backend.bookmark.domain.KotlinBookmarkType
import kr.co.bookand.backend.bookstore.domain.KotlinBookstore
import kr.co.bookand.backend.common.KotlinPageResponse

data class KotlinBookmarkFolderRequest(
    val folderName: String,
    @ApiModelProperty(value = "북마크 타입 (BOOKSTORE, ARTICLE)", example = "BOOKSTORE/ARTICLE")
    val bookmarkType: String
)

data class KotlinBookmarkIdResponse(
    val bookmarkId: Long,
)

data class KotlinBookmarkContentListRequest(
    val contentIdList: List<Long>,
    @ApiModelProperty(value = "북마크 타입 (BOOKSTORE, ARTICLE)", example = "BOOKSTORE/ARTICLE")
    val bookmarkType: String
)

data class KotlinBookmarkFolderResponse(
    val bookmarkId: Long,
    val folderName: String,
    val bookmarkType: KotlinBookmarkType,
    val bookmarkImage: String
) {
    constructor(bookmark: KotlinBookmark) : this(
        bookmarkId = bookmark.id,
        folderName = bookmark.folderName,
        bookmarkType = bookmark.bookmarkType,
        bookmarkImage = bookmark.folderImage.orEmpty()
    )
}

data class KotlinBookmarkFolderListResponse(
    val bookmarkFolderList: List<KotlinBookmarkFolderResponse>
)

data class KotlinBookmarkResponse(
    val bookmarkId: Long,
    val folderName: String,
    val bookmarkType: KotlinBookmarkType,
    val bookmarkImage: String,
    val bookmarkInfo: KotlinPageResponse<KotlinBookmarkInfo>
) {
    constructor(bookmark: KotlinBookmark, bookmarkInfo: KotlinPageResponse<KotlinBookmarkInfo>) : this(
        bookmarkId = bookmark.id,
        folderName = bookmark.folderName,
        bookmarkType = bookmark.bookmarkType,
        bookmarkImage = bookmark.folderImage.orEmpty(),
        bookmarkInfo = bookmarkInfo
    )
}

data class KotlinBookmarkInfo(
    val bookmarkId: Long,
    val title: String,
    val image: String,
    val location: String
) {
    constructor(bookstore: KotlinBookstore) : this(
        bookmarkId = bookstore.id,
        title = bookstore.name,
        image = bookstore.mainImage,
        location = bookstore.address
    )

    constructor(article: KotlinArticle) : this(
        bookmarkId = article.id,
        title = article.title,
        image = article.mainImage,
        location = "location"
    )
}

data class KotlinBookmarkFolderNameRequest(
    val folderName: String
)
