package kr.co.bookand.backend.bookmark.domain.dto

import io.swagger.annotations.ApiModelProperty
import kr.co.bookand.backend.article.domain.Article
import kr.co.bookand.backend.bookmark.domain.Bookmark
import kr.co.bookand.backend.bookmark.domain.BookmarkType
import kr.co.bookand.backend.bookstore.domain.Bookstore
import kr.co.bookand.backend.common.PageResponse

data class BookmarkFolderRequest(
    val folderName: String,
    @ApiModelProperty(value = "북마크 타입 (BOOKSTORE, ARTICLE)", example = "BOOKSTORE/ARTICLE")
    val bookmarkType: String
)

data class BookmarkIdResponse(
    val bookmarkId: Long,
)

data class BookmarkContentListRequest(
    val contentIdList: List<Long>,
    @ApiModelProperty(value = "북마크 타입 (BOOKSTORE, ARTICLE)", example = "BOOKSTORE/ARTICLE")
    val bookmarkType: String
)

data class BookmarkFolderResponse(
    val bookmarkId: Long,
    val folderName: String,
    val bookmarkType: BookmarkType,
    val bookmarkImage: String
) {
    constructor(bookmark: Bookmark) : this(
        bookmarkId = bookmark.id,
        folderName = bookmark.folderName,
        bookmarkType = bookmark.bookmarkType,
        bookmarkImage = bookmark.folderImage.orEmpty()
    )
}

data class BookmarkFolderListResponse(
    val bookmarkFolderList: List<BookmarkFolderResponse>
)

data class BookmarkResponse(
    val bookmarkId: Long,
    val folderName: String,
    val bookmarkType: BookmarkType,
    val bookmarkImage: String,
    val bookmarkInfo: PageResponse<BookmarkInfo>
) {
    constructor(bookmark: Bookmark, bookmarkInfo: PageResponse<BookmarkInfo>) : this(
        bookmarkId = bookmark.id,
        folderName = bookmark.folderName,
        bookmarkType = bookmark.bookmarkType,
        bookmarkImage = bookmark.folderImage.orEmpty(),
        bookmarkInfo = bookmarkInfo
    )
}

data class BookmarkInfo(
    val bookmarkId: Long,
    val title: String,
    val image: String,
    val location: String
) {
    constructor(bookstore: Bookstore) : this(
        bookmarkId = bookstore.id,
        title = bookstore.name,
        image = bookstore.mainImage,
        location = bookstore.address
    )

    constructor(article: Article) : this(
        bookmarkId = article.id,
        title = article.title,
        image = article.mainImage,
        location = "location"
    )
}

data class BookmarkFolderNameRequest(
    val folderName: String
)
