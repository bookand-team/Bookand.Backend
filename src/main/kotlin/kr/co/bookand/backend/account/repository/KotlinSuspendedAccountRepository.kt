package kr.co.bookand.backend.account.repository

import kr.co.bookand.backend.account.domain.KotlinAccount
import kr.co.bookand.backend.account.domain.KotlinSuspendedAccount
import kr.co.bookand.backend.account.domain.SuspendedAccount
import org.springframework.data.jpa.repository.JpaRepository

interface KotlinSuspendedAccountRepository : JpaRepository<KotlinSuspendedAccount, Long> {
    fun findByAccount(account: KotlinAccount): KotlinSuspendedAccount?
}