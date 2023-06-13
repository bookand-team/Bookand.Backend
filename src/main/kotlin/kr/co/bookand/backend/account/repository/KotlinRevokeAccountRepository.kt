package kr.co.bookand.backend.account.repository

import kr.co.bookand.backend.account.domain.KotlinRevokeAccount
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinRevokeAccountRepository : JpaRepository<KotlinRevokeAccount, Long> {
}