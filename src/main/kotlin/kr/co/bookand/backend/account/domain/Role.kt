package kr.co.bookand.backend.account.domain

enum class Role {
    USER, ADMIN, MANAGER, SUSPENDED;

    fun checkAdmin() {
        if (this != ADMIN) {
            throw RuntimeException("ErrorCode.ROLE_ACCESS_ERROR")
        }
    }

    fun checkAdminAndManager() {
        if (this != MANAGER && this != ADMIN) {
            throw RuntimeException("ErrorCode.ROLE_ACCESS_ERROR")
        }
    }
}