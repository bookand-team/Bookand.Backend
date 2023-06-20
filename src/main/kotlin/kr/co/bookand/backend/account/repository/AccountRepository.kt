package kr.co.bookand.backend.account.repository

import kr.co.bookand.backend.account.model.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByEmail(email: String): Account?
    fun findByNickname(nickname: String): Account?
    fun existsByNickname(nickname: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun countAllByVisibility(visibility: Boolean): Long
}