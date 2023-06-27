package kr.co.bookand.backend.feedback.dto

import io.swagger.annotations.ApiModelProperty
import kr.co.bookand.backend.common.DeviceOSFilter
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.feedback.model.Feedback

data class CreateFeedbackRequest(
    @ApiModelProperty(value = "피드백 유형 (PUSH, INFORMATION_ERROR, INCONVENIENCE, ETC)")
    val feedbackType: String,
    @ApiModelProperty(value = "피드백 대상 (HOME, MAP, BOOKMARK, MY_PAGE, ARTICLE, BOOKSTORE, ETC)")
    val feedbackTarget: String,
    val content: String,
    val deviceOS: DeviceOSFilter
)

data class FeedbackIdResponse(
    val id: Long
)

data class FeedbackResponse(
    val id: Long,
    val providerEmail: String?,
    val userId: Long,
    val deviceOS: DeviceOSFilter,
    val feedbackType: String,
    val feedbackTarget: String,
    val content: String,
    val createdAt: String,
    val isConfirmed: Boolean
) {
    constructor(feedback: Feedback) : this(
        id = feedback.id,
        providerEmail = feedback.account?.providerEmail,
        userId = feedback.account?.id ?: 0,
        deviceOS = feedback.deviceOS,
        feedbackType = feedback.feedbackType.toDetail(),
        feedbackTarget = feedback.feedbackTarget.toDetail(),
        content = feedback.content,
        createdAt = feedback.createdAt.toString(),
        isConfirmed = feedback.isConfirmed
    )
}

data class FeedbackListResponse(
    val data: PageResponse<FeedbackResponse>,
)