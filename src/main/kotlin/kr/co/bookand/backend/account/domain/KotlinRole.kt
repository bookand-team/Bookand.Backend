package kr.co.bookand.backend.account.domain

import kr.co.bookand.backend.common.KotlinErrorCode

enum class KotlinRole {
    USER, ADMIN, MANAGER, SUSPENDED;

    fun checkAdmin() {
        if (this != ADMIN) {
            throw RuntimeException("KotlinErrorCode.ROLE_ACCESS_ERROR")
        }
    }

    fun checkAdminAndManager() {
        if (this != MANAGER && this != ADMIN) {
            throw RuntimeException("KotlinErrorCode.ROLE_ACCESS_ERROR")
        }
    }
}