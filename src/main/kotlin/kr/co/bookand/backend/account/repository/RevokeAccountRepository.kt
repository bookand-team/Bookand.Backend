package kr.co.bookand.backend.account.repository

import kr.co.bookand.backend.account.model.RevokeAccount
import org.springframework.data.jpa.repository.JpaRepository

interface RevokeAccountRepository : JpaRepository<RevokeAccount, Long> {
}