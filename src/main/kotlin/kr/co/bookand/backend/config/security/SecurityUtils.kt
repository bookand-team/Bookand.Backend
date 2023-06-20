package kr.co.bookand.backend.config.security

import kr.co.bookand.backend.account.domain.Account
import kr.co.bookand.backend.account.repository.AccountRepository
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {
    fun getCurrentAccountEmail(): String =
        SecurityContextHolder.getContext().authentication?.name
            ?: throw RuntimeException("ErrorCode.NOT_FOUND_MEMBER")

    fun getCurrentAccount(accountRepository: AccountRepository): Account =
        accountRepository.findByEmail(getCurrentAccountEmail())
            ?: throw RuntimeException("ErrorCode.NOT_FOUND_MEMBER")
}
