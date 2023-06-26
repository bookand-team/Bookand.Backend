package kr.co.bookand.backend.notice.dto

import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.MemberIdFilter
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.notice.model.Notice


data class CreateNoticeRequest(
    val title: String,
    val content: String,
    val image: String,
    val noticeType: String,
    val noticeFilter: NoticeFilter
)

data class NoticeIdResponse(
    val id: Long
)

data class UpdateNoticeRequest(
    val title: String,
    val content: String,
    val image: String,
    val noticeType: String,
    val noticeFilter: NoticeFilter
)

data class NoticeFilter(
    val deviceOS: DeviceOSFilter,
    val memberId: MemberIdFilter
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
    val noticeFilter: NoticeFilter,
    val displayAt: String?
) {
    constructor(notice: Notice) : this(
        id = notice.id,
        title = notice.title,
        content = notice.content,
        image = notice.image,
        status = notice.status.name,
        noticeType = notice.noticeType.name,
        noticeFilter = NoticeFilter(
            deviceOS = notice.deviceOSFilter,
            memberId = notice.memberIdFilter
        ),
        displayAt = notice.displayAt?.toString()
    )
}