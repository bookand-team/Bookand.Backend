package kr.co.bookand.backend.account.repository

import kr.co.bookand.backend.account.domain.RevokeAccount
import org.springframework.data.jpa.repository.JpaRepository

interface RevokeAccountRepository : JpaRepository<RevokeAccount, Long> {
}