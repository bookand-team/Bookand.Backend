package kr.co.bookand.backend.config.jwt.exception

import kr.co.bookand.backend.common.ErrorCode


class JwtException(
    val errorCode: ErrorCode,
    exception: Any
) : RuntimeException(exception.let { errorCode.errorLog }) {

    fun convertToJson(ex: String): String {
        return "{" +
                "\"code\":\"" + errorCode.errorCode +
                "\",\"log\":\"" + ex +
                "\",\"message\":\"" + errorCode.errorMessage +
                "\"}"
    }
}
