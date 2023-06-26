package kr.co.bookand.backend.account.model

import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.exception.BookandException

enum class RevokeType(
    var reason: String
) {
    NOT_ENOUGH_CONTENT("콘텐츠가 만족스럽지 않아요"),
    UNCOMFORTABLE("이용 방법이 불편해요"),
    PRIVACY("개인정보 보안이 걱정돼요"),
    ETC("기타");

    companion object {
        fun checkRevokeType(revokeType: String): RevokeType {
            return when (revokeType) {
                "NOT_ENOUGH_CONTENT" -> NOT_ENOUGH_CONTENT
                "UNCOMFORTABLE" -> UNCOMFORTABLE
                "PRIVACY" -> PRIVACY
                "ETC" -> ETC
                else -> throw BookandException(ErrorCode.INPUT_VALID_ERROR)
            }
        }
    }
}
