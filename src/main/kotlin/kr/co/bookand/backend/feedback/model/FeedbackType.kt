package kr.co.bookand.backend.feedback.model

enum class FeedbackType(
    private val detail: String
) {

    PUSH("알림이 너무 자주 와요"),
    INFORMATION_ERROR("정보가 정확하지 않거나 부족해요"),
    INCONVENIENCE("이용방법이 불편해요"),
    ETC("기타");

    fun toDetail(): String {
        return detail
    }
}

