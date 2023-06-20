package kr.co.bookand.backend.account.repository

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.account.domain.SuspendedAccount
import org.springframework.data.jpa.repository.JpaRepository

interface SuspendedAccountRepository : JpaRepository<SuspendedAccount, Long> {
    fun findByAccount(account: Account): SuspendedAccount?
}