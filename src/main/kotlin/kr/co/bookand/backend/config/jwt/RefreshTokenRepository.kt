package kr.co.bookand.backend.config.jwt

import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, String> {
    fun findByKey(key: String): RefreshToken?

    fun existsByKey(key: String): Boolean

    fun deleteByAccountId(accountId: Long)
}