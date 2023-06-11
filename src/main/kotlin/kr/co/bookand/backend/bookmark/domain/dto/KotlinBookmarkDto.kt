package kr.co.bookand.backend.bookmark.domain.dto

data class KotlinBookmarkFolderRequest(
    val folderName: String,
    val bookmarkType: String
)

data class KotlinBookmarkIdResponse(
    val bookmarkId: Long,
)

data class KotlinBookmarkContentListRequest(
    val contentIdList : List<Long>,
    val bookmarkType: String
)