package kr.co.bookand.backend.config.jwt

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface KotlinRefreshTokenRepository : JpaRepository<KotlinRefreshToken, String> {
    fun findByKey(key: String): KotlinRefreshToken?

    fun existsByKey(key: String): Boolean

    fun deleteByAccountId(accountId: Long)
}