package kr.co.bookand.backend.feedback.domain.dto

import io.swagger.annotations.ApiModelProperty

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
    val providerEmail: String,
    val feedbackType: String,
    val feedbackTarget: String,
    val content: String
)