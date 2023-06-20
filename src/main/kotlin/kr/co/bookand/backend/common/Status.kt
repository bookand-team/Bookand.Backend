package kr.co.bookand.backend.common

import kr.co.bookand.backend.common.exception.BookandException


enum class Status {
    INVISIBLE, VISIBLE, REMOVE;

    companion object {
        fun toEnum(status: String): Status {
            return when (status) {
                INVISIBLE.name -> INVISIBLE
                VISIBLE.name -> VISIBLE
                REMOVE.name -> REMOVE
                else -> throw BookandException(ErrorCode.INPUT_VALID_ERROR)
            }
        }
    }
}
