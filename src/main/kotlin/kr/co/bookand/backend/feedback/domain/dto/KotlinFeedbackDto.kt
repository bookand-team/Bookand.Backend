package kr.co.bookand.backend.feedback.domain.dto

import io.swagger.annotations.ApiModelProperty
import kr.co.bookand.backend.common.PageResponse
import kr.co.bookand.backend.feedback.domain.Feedback

data class CreateFeedbackRequest(
    @ApiModelProperty(value = "피드백 유형 (PUSH, INFORMATION_ERROR, INCONVENIENCE, ETC)")
    val feedbackType: String,

    @ApiModelProperty(value = "피드백 대상 (HOME, MAP, BOOKMARK, MY_PAGE, ARTICLE, BOOKSTORE, ETC)")
    val feedbackTarget: String,
    val content: String
)

data class FeedbackIdResponse(
    val id: Long
)

data class FeedbackResponse(
    val id: Long,
    val providerEmail: String?,
    val feedbackType: String,
    val feedbackTarget: String,
    val content: String
){
    constructor(feedback: Feedback) : this(
        id = feedback.id,
        providerEmail = feedback.account?.providerEmail,
        feedbackType = feedback.feedbackType.toDetail(),
        feedbackTarget = feedback.feedbackTarget.toDetail(),
        content = feedback.content
    )
}

data class FeedbackListResponse(
    val data : PageResponse<FeedbackResponse>,
)