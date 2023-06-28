package kr.co.bookand.backend.notice.dto

import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.MemberIdFilter
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.common.Status
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
    val noticeType: String,
    val createdAt: String,
    val displayAt: String?,
    val modifiedAt: String,
    val isDisplayed: Boolean,
    val content: String,
    val image: String,
    val noticeFilter: NoticeFilter
) {
    constructor(notice: Notice) : this(
        id = notice.id,
        title = notice.title,
        noticeType = notice.noticeType.name,
        createdAt = notice.createdAt.toString(),
        displayAt = notice.displayAt?.toString(),
        modifiedAt = notice.modifiedAt.toString(),
        isDisplayed = notice.status == Status.VISIBLE,
        content = notice.content,
        image = notice.image,
        noticeFilter = NoticeFilter(
            deviceOS = notice.deviceOSFilter,
            memberId = notice.memberIdFilter
        )
    )
}