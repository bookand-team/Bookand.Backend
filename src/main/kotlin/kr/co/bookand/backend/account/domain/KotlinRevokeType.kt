package kr.co.bookand.backend.account.domain

enum class KotlinRevokeType(
    var reason: String
) {
    NOT_ENOUGH_CONTENT("콘텐츠가 만족스럽지 않아요"),
    UNCOMFORTABLE("이용 방법이 불편해요"),
    PRIVACY("개인정보 보안이 걱정돼요"),
    ETC("기타");

    companion object {
        fun checkRevokeType(revokeType: String): KotlinRevokeType {
            return when (revokeType) {
                "NOT_ENOUGH_CONTENT" -> NOT_ENOUGH_CONTENT
                "UNCOMFORTABLE" -> UNCOMFORTABLE
                "PRIVACY" -> PRIVACY
                "ETC" -> ETC
                else -> throw IllegalArgumentException("해당하는 타입이 없습니다.")
            }
        }
    }
}