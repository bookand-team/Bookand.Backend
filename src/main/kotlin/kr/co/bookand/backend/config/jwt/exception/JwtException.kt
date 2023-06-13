package kr.co.bookand.backend.config.jwt.exception

import kr.co.bookand.backend.common.KotlinErrorCode


class JwtException(
    val errorCode: KotlinErrorCode,
    exception: Any
) : RuntimeException(exception.let { errorCode.errorLog } ?: exception.toString()) {

    fun convertToJson(ex: String): String {
        return "{" +
                "\"code\":\"" + errorCode.errorCode +
                "\",\"log\":\"" + ex +
                "\",\"message\":\"" + errorCode.errorMessage +
                "\"}"
    }
}
