package kr.co.bookand.backend.account.repository

import kr.co.bookand.backend.account.domain.KotlinAccount
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinAccountRepository : JpaRepository<KotlinAccount, Long> {
    fun findByEmail(email: String): KotlinAccount?
    fun findByNickname(nickname: String): KotlinAccount?
    fun existsByNickname(nickname: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun countAllByVisibility(visibility: Boolean): Long
}