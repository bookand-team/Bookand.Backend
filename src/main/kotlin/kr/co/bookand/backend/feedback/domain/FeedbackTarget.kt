package kr.co.bookand.backend.feedback.domain

enum class FeedbackTarget (
    private val detail: String
) {
    HOME("홈"),
    MAP("맵"),
    BOOKMARK("북마크"),
    MY_PAGE("마이페이지"),
    ARTICLE("아티클"),
    BOOKSTORE("서점 정보"),
    ETC("기타");

    fun toDetail(): String {
        return detail
    }
}