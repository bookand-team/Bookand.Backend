package kr.co.bookand.backend.notice.domain.dto

import kr.co.bookand.backend.notice.domain.NoticeType

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

data class NoticeMessageResponse(
    val message: String
)