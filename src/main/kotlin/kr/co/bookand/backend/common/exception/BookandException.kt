package kr.co.bookand.backend.common.exception

import kr.co.bookand.backend.common.ErrorCode

class BookandException(
    var errorCode: ErrorCode
) : RuntimeException(errorCode.errorLog)