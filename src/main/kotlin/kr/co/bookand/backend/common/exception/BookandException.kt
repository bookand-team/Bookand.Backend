package kr.co.bookand.backend.common.exception

import kr.co.bookand.backend.common.ErrorCode

class BookandException(
    private var errorCode: ErrorCode, exception: Any? = null
) : RuntimeException(exception.let { errorCode.errorLog }) {
}