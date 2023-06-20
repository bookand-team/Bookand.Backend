package kr.co.bookand.backend.account.domain

import kr.co.bookand.backend.common.ErrorCode
import kr.co.bookand.backend.common.exception.BookandException

enum class Role {
    USER, ADMIN, MANAGER, SUSPENDED;

    fun checkAdmin() {
        if (this != ADMIN) {
            throw BookandException(ErrorCode.ROLE_ACCESS_ERROR)
        }
    }

    fun checkAdminAndManager() {
        if (this != MANAGER && this != ADMIN) {
            throw BookandException(ErrorCode.ROLE_ACCESS_ERROR)
        }
    }
}