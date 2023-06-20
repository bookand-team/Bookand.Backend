package kr.co.bookand.backend.notice.dto

import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.notice.model.Notice


data class CreateNoticeRequest(
    val title: String,
    val content: String,
    val image: String,
    val noticeType: String,
    val targetType: String,
    val targetNum: String
)

data class NoticeIdResponse(
    val id: Long
)

data class UpdateNoticeRequest(
    val title: String,
    val content: String,
    val image: String,
    val noticeType: String,
    val targetType: String,
    val targetNum: String
)

data class NoticePageResponse(
    val data: PageResponse<NoticeResponse>
)

data class NoticeResponse(
    val id: Long,
    val title: String,
    val content: String,
    val image: String,
    val status: String,
    val noticeType: String,
    val targetType: String,
    val targetNum: String,
    val displayAt: String?
) {
    constructor(notice: Notice) : this(
        id = notice.id,
        title = notice.title,
        content = notice.content,
        image = notice.image,
        status = notice.status.name,
        noticeType = notice.noticeType.name,
        targetType = notice.deviceOSFilter.name,
        targetNum = notice.memberIdFilter.name,
        displayAt = notice.displayAt?.toString()
    )
}