package kr.co.bookand.backend.notice.domain.dto

import kr.co.bookand.backend.common.KotlinPageResponse
import kr.co.bookand.backend.notice.domain.KotlinNotice
import org.springframework.data.domain.Page


data class KotlinCreateNoticeRequest(
    val title: String,
    val content: String,
    val image: String,
    val noticeType: String,
    val targetType: String,
    val targetNum: String
)

data class KotlinNoticeIdResponse(
    val id: Long
)

data class KotlinUpdateNoticeRequest(
    val title: String,
    val content: String,
    val image: String,
    val noticeType: String,
    val targetType: String,
    val targetNum: String
)

data class KotlinNoticeListResponse(
    val data: KotlinPageResponse<KotlinNoticeResponse>
)

data class KotlinNoticeResponse(
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
    constructor(notice: KotlinNotice) : this(
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