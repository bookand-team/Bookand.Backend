package kr.co.bookand.backend.common.exception

import kr.co.bookand.backend.common.ErrorCode

data class ApiErrorResponse(
    val code: String,
    val log: String,
    val message: String,
){
    constructor(errorCode: ErrorCode, log: String) : this(
        code = errorCode.errorCode,
        message = errorCode.errorMessage,
        log = log
    )
}
