package kr.co.bookand.backend.feedback.domain.dto

import io.swagger.annotations.ApiModelProperty
import kr.co.bookand.backend.common.KotlinPageResponse
import kr.co.bookand.backend.feedback.domain.KotlinFeedback
import org.springframework.data.domain.Page

data class KotlinCreateFeedbackRequest(
    @ApiModelProperty(value = "피드백 유형 (PUSH, INFORMATION_ERROR, INCONVENIENCE, ETC)")
    val feedbackType: String,

    @ApiModelProperty(value = "피드백 대상 (HOME, MAP, BOOKMARK, MY_PAGE, ARTICLE, BOOKSTORE, ETC)")
    val feedbackTarget: String,
    val content: String
)

data class KotlinFeedbackIdResponse(
    val id: Long
)

data class KotlinFeedbackResponse(
    val id: Long,
    val providerEmail: String?,
    val feedbackType: String,
    val feedbackTarget: String,
    val content: String
){
    constructor(feedback: KotlinFeedback) : this(
        id = feedback.id,
        providerEmail = feedback.account?.providerEmail,
        feedbackType = feedback.feedbackType.toDetail(),
        feedbackTarget = feedback.feedbackTarget.toDetail(),
        content = feedback.content
    )
}

data class KotlinFeedbackListResponse(
    val data : KotlinPageResponse<KotlinFeedbackResponse>,
)